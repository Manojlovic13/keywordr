package com.keywordr.exception;

public class KeywordrRuntimeException extends RuntimeException {
    public KeywordrRuntimeException() {
        super();
    }

    public KeywordrRuntimeException(String message) {
        super(message);
    }

    public KeywordrRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeywordrRuntimeException(Throwable cause) {
        super(cause);
    }
}
