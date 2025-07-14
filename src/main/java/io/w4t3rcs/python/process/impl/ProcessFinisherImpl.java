package io.w4t3rcs.python.process.impl;

import io.w4t3rcs.python.process.ProcessFinisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessFinisherImpl implements ProcessFinisher {
    @Override
    public void finish(Process process) {
        int exitCode = process.exitValue();
        if (exitCode == 0) log.info("Python script is executed with code: {}", exitCode);
        else log.error("Something went wrong! Python script is executed with code: {}", exitCode);
    }
}
