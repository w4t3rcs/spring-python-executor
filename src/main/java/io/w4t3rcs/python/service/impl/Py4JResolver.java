package io.w4t3rcs.python.service.impl;

import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.service.PythonCompletionResolver;
import io.w4t3rcs.python.service.PythonFileHandler;
import io.w4t3rcs.python.util.Py4JUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Py4JResolver implements PythonCompletionResolver {
    private final PythonFileHandler pythonFileHandler;
    private final Py4JProperties py4JProperties;

    @SneakyThrows
    @Override
    public String resolve(String script, Object... args) {
        if (py4JProperties.isAutoImport() && !pythonFileHandler.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + script;
        } else if (pythonFileHandler.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + pythonFileHandler.readScriptBodyFromFile(script);
        } else {
            return script;
        }
    }
}
