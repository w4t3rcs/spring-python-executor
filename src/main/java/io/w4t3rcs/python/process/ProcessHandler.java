package io.w4t3rcs.python.process;

public interface ProcessHandler<R> {
    R handle(Process process);
}
