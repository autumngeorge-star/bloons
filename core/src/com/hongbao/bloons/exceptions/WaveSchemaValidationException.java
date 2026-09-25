package com.hongbao.bloons.exceptions;

public class WaveSchemaValidationException extends RuntimeException {

    public WaveSchemaValidationException(String message) {
        super(message);
    }

    public WaveSchemaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
