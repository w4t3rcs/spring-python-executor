package io.w4t3rcs.python.process.impl;

import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.process.ProcessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ErrorProcessHandler implements ProcessHandler<Void> {
    @Override
    public Void handle(Process process) {
        try (BufferedReader bufferedReader = process.errorReader()) {
            String errorMessage = bufferedReader.lines().collect(Collectors.joining());
            if (!errorMessage.isBlank()) {
                log.error(errorMessage);
            }
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }

        return null;
    }
}
