package io.w4t3rcs.python.exception;

public class ProcessInterruptedException extends RuntimeException {
    public ProcessInterruptedException(Throwable cause) {
        super(cause);
    }
}
