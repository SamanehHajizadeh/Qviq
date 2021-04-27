package com.springboot.qviq.exception;


public class InfoNotFoundException extends RuntimeException {
    public InfoNotFoundException(Long id) {
        super("Message id not found : " + id);
    }
}
