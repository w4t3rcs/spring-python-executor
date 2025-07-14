package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.ResultProperties;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.util.PythonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("resultResolver")
@RequiredArgsConstructor
public class ResultResolver implements PythonResolver {
    private final ResultProperties resultProperties;
    private final PythonFileHandler pythonFileHandler;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        if (pythonFileHandler.isPythonFile(script)) {
            return pythonFileHandler.readScriptBodyFromFile(script, this::resolveResult);
        } else {
            return resolveResult(script);
        }
    }

    private String resolveResult(String script) {
        return PythonUtil.replaceScriptFragments(script, resultProperties.regex(),
                resultProperties.positionFromStart(), resultProperties.positionFromEnd(),
                (matcher, fragment) ->
                        "print('" + resultProperties.appearance() + "' + json.dumps(" + fragment + "))");
    }
}
