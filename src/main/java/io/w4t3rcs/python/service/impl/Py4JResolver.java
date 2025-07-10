package io.w4t3rcs.python.service.impl;

import io.w4t3rcs.python.config.Py4JCondition;
import io.w4t3rcs.python.config.Py4JProperties;
import io.w4t3rcs.python.service.PythonFileHandler;
import io.w4t3rcs.python.service.PythonResolver;
import io.w4t3rcs.python.util.Py4JUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("py4JResolver")
@Conditional(Py4JCondition.class)
@RequiredArgsConstructor
public class Py4JResolver implements PythonResolver {
    private final PythonFileHandler pythonFileHandler;
    private final Py4JProperties py4JProperties;

    @SneakyThrows
    @Override
    public String resolve(String script, @Nullable Map<String, Object> arguments) {
        if (py4JProperties.autoImport() && !pythonFileHandler.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + script;
        } else if (pythonFileHandler.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + pythonFileHandler.readScriptBodyFromFile(script);
        } else {
            return script;
        }
    }
}
