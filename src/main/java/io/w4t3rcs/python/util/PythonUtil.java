package io.w4t3rcs.python.util;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class PythonUtil {
    public static final String IMPORT_JSON = "import json\n";

    public <R> R executeScript(String script, Class<? extends R> resultClass, PythonExecutor pythonExecutor, Map<String, Object> arguments, PythonResolver... pythonResolvers) {
        String result = script;
        for (PythonResolver pythonResolver : pythonResolvers) {
            result = pythonResolver.resolve(result, arguments);
        }

        return pythonExecutor.execute(result, resultClass);
    }

    public String replaceScriptFragments(String script, String regex, int positionFromStart, int positionFromEnd, BiFunction<Matcher, String, String> bodyMapper) {
        StringBuilder resolvedScript = new StringBuilder(script);
        if (!script.contains(IMPORT_JSON)) resolvedScript.insert(0, IMPORT_JSON);
        Pattern spelPattern = Pattern.compile(regex);
        Matcher matcher = spelPattern.matcher(resolvedScript);
        if (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(positionFromStart, group.length() - positionFromEnd);
            String result = bodyMapper.apply(matcher, expressionString);
            resolvedScript.replace(matcher.start(), matcher.end(), result);
        }

        return resolvedScript.toString();
    }
}
