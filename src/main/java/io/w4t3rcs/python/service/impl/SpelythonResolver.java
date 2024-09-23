package io.w4t3rcs.python.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.service.PythonCompletionResolver;
import io.w4t3rcs.python.service.PythonFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SpelythonResolver implements PythonCompletionResolver {
    private static final String IMPORT_JSON = "import json";
    private static final String RESULT_EXPRESSION = "#result";
    private static final Pattern SPEL_PATTERN = Pattern.compile("spel\\{.+}");
    private static final int SPEL_START_DELIMITER_INDEX = 5;
    private final PythonFileHandler pythonFileHandler;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String resolve(String script, Object... args) {
        Object methodResult;
        if (args != null && args.length > 0) methodResult = args[0];
        else methodResult = null;
        if (!pythonFileHandler.isPythonFile(script)) {
            return resolveSpELExpressions(script, methodResult);
        } else {
            return pythonFileHandler.readScriptBodyFromFile(script, scriptLine -> resolveSpELExpressions(scriptLine, methodResult));
        }
    }

    @SneakyThrows
    private String resolveSpELExpressions(String script, Object methodResult) {
        String resolvedScript = script;
        if (!script.contains(IMPORT_JSON)) resolvedScript = IMPORT_JSON + "\n" + resolvedScript;
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        parser.parseExpression(RESULT_EXPRESSION).setValue(context, methodResult);
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        Matcher matcher = SPEL_PATTERN.matcher(script);
        while (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(SPEL_START_DELIMITER_INDEX, group.length() - 1);
            Expression expression = parser.parseExpression(expressionString);
            Object result  = expression.getValue(context, Object.class);
            String jsonResult = objectMapper.writeValueAsString(result);
            String jsonPythonObject = "'%s'".formatted(jsonResult);
            String jsonLoadedObject = "json.loads(%s)".formatted(jsonPythonObject).replace("\"", "\"\""); //XD in what universe this should work
            resolvedScript = resolvedScript.replace(group, jsonLoadedObject);
        }

        return resolvedScript;
    }
}
