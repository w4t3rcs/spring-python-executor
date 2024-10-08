package io.w4t3rcs.python.service.impl;

import io.w4t3rcs.python.config.PythonProperties;
import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.service.PythonExecutor;
import io.w4t3rcs.python.service.PythonFileHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class PythonExecutorImpl implements PythonExecutor {
    private final PythonFileHandler pythonFileHandler;
    private final PythonProperties pythonProperties;

    @Override
    public void execute(String script) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String startCommand = pythonProperties.getStartCommand();
            if (pythonFileHandler.isPythonFile(script)) {
                processBuilder.command(startCommand, pythonFileHandler.getScriptPath(script).toString());
            } else {
                processBuilder.command(startCommand, "-c", script);
            }

            log.info("Python script is going to be executed");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (pythonProperties.isLoggable()) {
                logPythonCommand(process);
                logPythonError(process);
            }

            if (exitCode == 0) log.info("Python script ({}) is executed with code: {}", script, exitCode);
            else log.error("Something went wrong with Python script ({}) is executed with code: {}", script, exitCode);
        } catch (IOException | InterruptedException e) {
            throw new PythonReadingException(e);
        }
    }

    private void logPythonCommand(Process process) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            bufferedReader.lines().forEach(log::info);
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }
    }

    private void logPythonError(Process process) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String errorMessage = bufferedReader.lines().collect(Collectors.joining());
            if (!errorMessage.isBlank()) log.error(errorMessage);
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }
    }
}
