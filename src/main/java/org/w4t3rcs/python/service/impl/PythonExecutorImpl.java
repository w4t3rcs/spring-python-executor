package org.w4t3rcs.python.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w4t3rcs.python.config.PythonProperties;
import org.w4t3rcs.python.service.PythonExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class PythonExecutorImpl implements PythonExecutor {
    private final PythonProperties pythonProperties;

    @SneakyThrows
    @Override
    public void execute(String script) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String startCommand = pythonProperties.getStartCommand();
        if (script.endsWith(".py")) {
            ClassPathResource classPathResource = new ClassPathResource(script);
            processBuilder.command(startCommand, classPathResource.getFile().getAbsolutePath());
        } else {
            processBuilder.command(startCommand, "-c", script);
        }

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (pythonProperties.isLoggable()) {
            logPythonCommand(process);
            logPythonError(process);
        }

        if (exitCode == 0) log.info("Python script ({}) is executed with code: {}", script, exitCode);
        else log.warn("Something went wrong with python Python script ({}) is executed with code: {}", script, exitCode);
    }

    private void logPythonCommand(Process process) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            bufferedReader.lines().forEach(log::info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logPythonError(Process process) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            bufferedReader.lines().forEach(log::warn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
