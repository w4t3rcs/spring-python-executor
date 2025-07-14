package io.w4t3rcs.python.resolver.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.config.SpelythonProperties;
import io.w4t3rcs.python.exception.SpelythonProcessingException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.util.PythonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("spelythonResolver")
@RequiredArgsConstructor
public class SpelythonResolver implements PythonResolver {
    private final SpelythonProperties spelythonProperties;
    private final PythonFileHandler pythonFileHandler;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        if (pythonFileHandler.isPythonFile(script)) {
            return pythonFileHandler.readScriptBodyFromFile(script, scriptLine -> resolveSpELExpressions(scriptLine, arguments));
        } else {
            return resolveSpELExpressions(script, arguments);
        }
    }

    private String resolveSpELExpressions(String script, Map<String, Object> arguments) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (arguments != null && !arguments.isEmpty()) {
            arguments.forEach((key, value) ->
                    parser.parseExpression(spelythonProperties.spelLocalVariableIndex() + key)
                            .setValue(context, value));
        }
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        return PythonUtil.replaceScriptFragments(script, spelythonProperties.regex(),
                spelythonProperties.spelPositionFromStart(), spelythonProperties.spelPositionFromEnd(),
                ((matcher, fragment) -> {
                    try {
                        Expression expression = parser.parseExpression(fragment);
                        Object result = expression.getValue(context, Object.class);
                        String jsonResult = objectMapper.writeValueAsString(result);
                        String jsonPythonObject = "'" + jsonResult + "'";
                        return "json.loads(" + jsonPythonObject.replace("\"", "\"\"") + ")";
                    } catch (JsonProcessingException e) {
                        throw new SpelythonProcessingException(e);
                    }
                }));
    }
}
