package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class ValidationException extends ResponseStatusException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors, String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errors = errors;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}