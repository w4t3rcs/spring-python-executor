package org.w4t3rcs.python.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.w4t3rcs.python.config.Py4JProperties;
import org.w4t3rcs.python.service.PythonCompletionResolver;
import org.w4t3rcs.python.util.Py4JUtil;
import org.w4t3rcs.python.util.ScriptUtil;

@Service
@RequiredArgsConstructor
public class Py4JResolver implements PythonCompletionResolver {
    private final Py4JProperties py4JProperties;

    @SneakyThrows
    @Override
    public String resolve(String script) {
        if (py4JProperties.isAutoImport() && !ScriptUtil.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + script;
        } else if (ScriptUtil.isPythonFile(script)) {
            return Py4JUtil.IMPORT_PY4J + ScriptUtil.getScriptBodyFromFile(script);
        } else {
            return script;
        }
    }
}