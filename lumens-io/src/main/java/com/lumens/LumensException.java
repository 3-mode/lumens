package com.lumens;

public class LumensException extends RuntimeException {

    public LumensException() {
        super();
    }

    public LumensException(String message) {
        super(message);
    }

    public LumensException(String message, Throwable cause) {
        super(message, cause);
    }

    public LumensException(Throwable cause) {
        super(cause);
    }
}
