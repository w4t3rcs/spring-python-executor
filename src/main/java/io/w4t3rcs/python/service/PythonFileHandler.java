package io.w4t3rcs.python.service;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public interface PythonFileHandler {
    String PYTHON_FILE_FORMAT = ".py";

    boolean isPythonFile(String path);

    void writeScriptBodyToFile(String path, String script);

    void writeScriptBodyToFile(Path path, String script);

    String readScriptBodyFromFile(String path);

    String readScriptBodyFromFile(Path path);

    String readScriptBodyFromFile(String path, UnaryOperator<String> mapper);

    String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper);

    Path getScriptPath(String path);
}
