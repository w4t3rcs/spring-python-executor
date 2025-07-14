package io.w4t3rcs.python.executor;

public interface PythonExecutor {
    <R> R execute(String script, Class<? extends R> resultClass);
}
