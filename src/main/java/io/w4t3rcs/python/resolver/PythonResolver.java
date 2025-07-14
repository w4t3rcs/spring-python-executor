package io.w4t3rcs.python.resolver;

import java.util.Map;

public interface PythonResolver {
    String resolve(String script, Map<String, Object> arguments);
}
