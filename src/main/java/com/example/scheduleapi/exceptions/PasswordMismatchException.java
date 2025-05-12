package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PasswordMismatchException extends ResponseStatusException {
    public PasswordMismatchException(HttpStatusCode status, String message) {
        super(status, message, null);
    }
}
