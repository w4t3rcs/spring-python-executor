package org.w4t3rcs.python.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@UtilityClass
public class ScriptUtil {
    public boolean isPythonFile(String script) {
        return script.endsWith(".py");
    }

    public String getScriptBodyFromFile(String script) throws IOException {
        String scriptPath = getScriptPath(script);
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(scriptPath))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    public String getScriptBodyFromFile(String script, UnaryOperator<String> mapper) throws IOException {
        String scriptPath = getScriptPath(script);
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(scriptPath))) {
            return bufferedReader.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
        }
    }

    public String getScriptPath(String script) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(script);
        return classPathResource.getFile().getAbsolutePath();
    }
}
