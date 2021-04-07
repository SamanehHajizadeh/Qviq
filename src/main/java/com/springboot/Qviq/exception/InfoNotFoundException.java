package com.springboot.Qviq.exception;


public class InfoNotFoundException extends RuntimeException {
    public InfoNotFoundException(Long id) {
        super("Message id not found : " + id);
    }

    public InfoNotFoundException() {
    }

    public InfoNotFoundException(String message) {
        super(message);
    }

    public InfoNotFoundException(String message, Throwable cause) {
        super("500", cause);
    }
}
