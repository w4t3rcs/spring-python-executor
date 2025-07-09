package io.w4t3rcs.python.service;

import jakarta.annotation.Nullable;

import java.util.Map;

public interface PythonResolver {
    String resolve(String script, @Nullable Map<String, Object> arguments);
}
