package io.w4t3rcs.python.util;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class PythonUtil {
    <R> R executeScript(String script, Class<? extends R> resultClass, PythonExecutor pythonExecutor, Map<String, Object> arguments, PythonResolver... pythonResolvers) {
        String result = script;
        for (PythonResolver pythonResolver : pythonResolvers) {
            result = pythonResolver.resolve(result, arguments);
        }

        return pythonExecutor.execute(result, resultClass);
    }
}
