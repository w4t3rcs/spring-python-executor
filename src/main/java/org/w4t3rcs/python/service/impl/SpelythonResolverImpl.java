package org.w4t3rcs.python.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.w4t3rcs.python.service.SpelythonResolver;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpelythonResolverImpl implements SpelythonResolver {
    private final ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public String resolve(String script) {
        if (!script.endsWith(".py")) {
            return getResolvedScript(script);
        } else {
            ClassPathResource classPathResource = new ClassPathResource(script);
            String scriptPath = classPathResource.getFile().getAbsolutePath();
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(scriptPath))) {
                return bufferedReader.lines()
                        .map(this::getResolvedScript)
                        .collect(Collectors.joining("\n"));
            }
        }
    }

    private String getResolvedScript(String script) {
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
