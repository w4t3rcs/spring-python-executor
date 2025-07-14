package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.ResultProperties;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("resultResolver")
@RequiredArgsConstructor
public class ResultResolver implements PythonResolver {
    private static final String IMPORT_JSON = "import json\n";
    private final ResultProperties resultProperties;
    private final PythonFileHandler pythonFileHandler;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        if (pythonFileHandler.isPythonFile(script)) {
            return pythonFileHandler.readScriptBodyFromFile(script, scriptLine -> resolveResult(scriptLine));
        } else {
            return resolveResult(script);
        }
    }

    private String resolveResult(String script) {
        StringBuilder resolvedScript = new StringBuilder(script);
        if (!script.contains(IMPORT_JSON)) resolvedScript.insert(0, IMPORT_JSON);
        Pattern spelPattern = Pattern.compile(resultProperties.regex());
        Matcher matcher = spelPattern.matcher(resolvedScript);
        if (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(resultProperties.positionFromStart(), group.length() - resultProperties.positionFromEnd());
            String printedObject ="print('" + resultProperties.appearance() + "' + json.dumps(" + expressionString + "))";
            resolvedScript.replace(matcher.start(), matcher.end(), printedObject);
        }

        return resolvedScript.toString();
    }
}
