package io.w4t3rcs.python.process.impl;

import io.w4t3rcs.python.config.PythonProperties;
import io.w4t3rcs.python.exception.ProcessStartException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.process.ProcessStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Implementation of the {@link ProcessStarter} interface that starts Python processes.
 * This class is responsible for creating and starting Python processes either from
 * script files or from inline Python code.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessStarterImpl implements ProcessStarter {
    private final PythonProperties pythonProperties;
    private final PythonFileHandler pythonFileHandler;

    @Override
    public Process start(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String startCommand = pythonProperties.startCommand();
            if (pythonFileHandler.isPythonFile(command)) {
                processBuilder.command(startCommand, pythonFileHandler.getScriptPath(command).toString());
            } else {
                processBuilder.command(startCommand, "-c", command);
            }

            log.info("Python script is going to be executed");
            return processBuilder.start();
        } catch (IOException e) {
            throw new ProcessStartException(e);
        }
    }
}
