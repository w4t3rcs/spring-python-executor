package io.w4t3rcs.python.service.impl;

import io.w4t3rcs.python.service.PythonCompletionResolver;
import io.w4t3rcs.python.util.ScriptUtil;
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
    private final ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public String resolve(String script) {
        if (!ScriptUtil.isPythonFile(script)) {
            return resolveSpELExpressions(script);
        } else {
            return ScriptUtil.getScriptBodyFromFile(script, this::resolveSpELExpressions);
        }
    }



    private String resolveSpELExpressions(String script) {
        String resolvedScript = script;
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        Pattern pattern = Pattern.compile("\\$\\{.+}");
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(2, group.length() - 1);
            Expression expression = parser.parseExpression(expressionString);
            String result  = expression.getValue(context, String.class);
            resolvedScript = resolvedScript.replace(group, result);
        }

        return resolvedScript;
    }
}
