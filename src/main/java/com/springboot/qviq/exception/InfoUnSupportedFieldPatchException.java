package com.springboot.qviq.exception;

import java.util.Set;

public class InfoUnSupportedFieldPatchException extends RuntimeException {
    public InfoUnSupportedFieldPatchException(long id) {
        super("Field " + id + " update is not allow.");
    }}