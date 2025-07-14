package io.w4t3rcs.python.resolver.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.config.SpelythonProperties;
import io.w4t3rcs.python.exception.SpelythonProcessingException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("spelythonResolver")
@RequiredArgsConstructor
public class SpelythonResolver implements PythonResolver {
    private static final String IMPORT_JSON = "import json\n";
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
        try {
            StringBuilder resolvedScript = new StringBuilder(script);
            if (!script.contains(IMPORT_JSON)) resolvedScript.insert(0, IMPORT_JSON);
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            if (arguments != null && !arguments.isEmpty()) {
                arguments.forEach((key, value) -> {
                    parser.parseExpression(spelythonProperties.spelLocalVariableIndex() + key)
                            .setValue(context, value);
                });
            }
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
            Pattern spelPattern = Pattern.compile(spelythonProperties.regex());
            Matcher matcher = spelPattern.matcher(resolvedScript);
            while (matcher.find()) {
                String group = matcher.group();
                String expressionString = group.substring(spelythonProperties.spelPositionFromStart(), group.length() - spelythonProperties.spelPositionFromEnd());
                Expression expression = parser.parseExpression(expressionString);
                Object result = expression.getValue(context, Object.class);
                String jsonResult = objectMapper.writeValueAsString(result);
                String jsonPythonObject = "'" + jsonResult + "'";
                String jsonLoadedObject = "json.loads(" + jsonPythonObject.replace("\"", "\"\"") + ")"; //Uh, that's weird
                resolvedScript.replace(matcher.start(), matcher.end(), jsonLoadedObject);
            }
            return resolvedScript.toString();
        } catch (JsonProcessingException e) {
            throw new SpelythonProcessingException(e);
        }
    }
}
