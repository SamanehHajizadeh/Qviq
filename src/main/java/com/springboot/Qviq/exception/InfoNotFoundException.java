package com.springboot.Qviq.exception;


public class InfoNotFoundException extends RuntimeException {
    public InfoNotFoundException(Long id) {
        super("Message id not found : " + id);
    }
}
