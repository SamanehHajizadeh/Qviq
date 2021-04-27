package com.springboot.qviq.exception;

import java.util.Set;

public class InfoUnSupportedFieldPatchException extends RuntimeException {
    public InfoUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }}