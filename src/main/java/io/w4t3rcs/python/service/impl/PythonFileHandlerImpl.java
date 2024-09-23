package io.w4t3rcs.python.service.impl;

import io.w4t3rcs.python.config.PythonProperties;
import io.w4t3rcs.python.service.PythonFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PythonFileHandlerImpl implements PythonFileHandler {
    private final PythonProperties pythonProperties;

    @Override
    public boolean isPythonFile(String filename) {
        return filename.endsWith(PYTHON_FILE_FORMAT);
    }

    @Override
    public void writeScriptBodyToFile(String path, String script) {
        writeScriptBodyToFile(Path.of(path), script);
    }

    @SneakyThrows
    @Override
    public void writeScriptBodyToFile(Path path, String script) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(script);
        }
    }

    @Override
    public String readScriptBodyFromFile(String path) {
        return readScriptBodyFromFile(getScriptPath(path));
    }

    @SneakyThrows
    @Override
    public String readScriptBodyFromFile(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    @SneakyThrows
    @Override
    public String readScriptBodyFromFile(String path, UnaryOperator<String> mapper) {
        return readScriptBodyFromFile(getScriptPath(path), mapper);
    }

    @SneakyThrows
    @Override
    public String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return bufferedReader.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
        }
    }

    @SneakyThrows
    @Override
    public Path getScriptPath(String path) {
        ClassPathResource classPathResource = new ClassPathResource(pythonProperties.getPath() + path);
        return classPathResource.getFile().toPath();
    }
}
