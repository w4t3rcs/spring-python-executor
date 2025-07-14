package io.w4t3rcs.python.exception;

/**
 * Exception thrown when a process is interrupted during execution.
 * This typically occurs when a thread executing a process is interrupted
 * or when the process is forcibly terminated.
 */
public class ProcessInterruptedException extends RuntimeException {
    public ProcessInterruptedException(Throwable cause) {
        super(cause);
    }
}
