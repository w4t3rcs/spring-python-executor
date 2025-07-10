package io.w4t3rcs.python.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.config.SpelythonProperties;
import io.w4t3rcs.python.service.PythonFileHandler;
import io.w4t3rcs.python.service.PythonResolver;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

@Service
@RequiredArgsConstructor
public class SpelythonResolver implements PythonResolver {
    private static final String IMPORT_JSON = "import json";
    private final SpelythonProperties spelythonProperties;
    private final PythonFileHandler pythonFileHandler;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String resolve(String script, @Nullable Map<String, Object> arguments) {
        if (!pythonFileHandler.isPythonFile(script)) {
            return resolveSpELExpressions(script, arguments);
        } else {
            return pythonFileHandler.readScriptBodyFromFile(script, scriptLine -> resolveSpELExpressions(scriptLine, arguments));
        }
    }

    @SneakyThrows
    private String resolveSpELExpressions(String script, @Nullable Map<String, Object> arguments) {
        String resolvedScript = script;
        if (!resolvedScript.contains(IMPORT_JSON)) resolvedScript = IMPORT_JSON + "\n" + resolvedScript;
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
            resolvedScript = resolvedScript.replace(group, jsonLoadedObject);
        }

        return resolvedScript;
    }
}
