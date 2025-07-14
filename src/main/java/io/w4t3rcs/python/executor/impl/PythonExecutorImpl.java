package io.w4t3rcs.python.executor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.exception.ProcessInterruptedException;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.process.ProcessFinisher;
import io.w4t3rcs.python.process.ProcessHandler;
import io.w4t3rcs.python.process.ProcessStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PythonExecutorImpl implements PythonExecutor {
    private final ProcessStarter processStarter;
    private final ProcessHandler<String> inputProcessHandler;
    private final ProcessHandler<Void> errorProcessHandler;
    private final ObjectMapper objectMapper;
    private final ProcessFinisher processFinisher;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            Process process = processStarter.start(script);
            process.waitFor();
            String jsonResult = inputProcessHandler.handle(process);
            errorProcessHandler.handle(process);
            processFinisher.finish(process);
            return jsonResult != null ? objectMapper.convertValue(jsonResult, resultClass) : null;
        } catch (InterruptedException e) {
            throw new ProcessInterruptedException(e);
        }
    }
}
