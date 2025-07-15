package io.w4t3rcs.python.process.impl;

import io.w4t3rcs.python.config.PythonProperties;
import io.w4t3rcs.python.config.ResultProperties;
import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.process.ProcessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of the {@link ProcessHandler} interface that handles standard output from processes.
 * This class reads the standard output stream of a process, extracts result data marked with
 * a specific prefix, and optionally logs all output lines.
 * 
 * <p>It is typically used to capture the results of Python script execution.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InputProcessHandler implements ProcessHandler<String> {
    private final PythonProperties pythonProperties;
    private final ResultProperties resultProperties;

    @Override
    public String handle(Process process) {
        AtomicReference<String> result = new AtomicReference<>();
        try (BufferedReader bufferedReader = process.inputReader()) {
            bufferedReader.lines().forEach(line -> {
                if (line.contains(resultProperties.appearance())) {
                    String resultJson = line.replace(resultProperties.appearance(), "");
                    result.set(resultJson);
                }
                if (pythonProperties.loggable()) {
                    log.info(line);
                }
            });
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }

        return result.get();
    }
}
