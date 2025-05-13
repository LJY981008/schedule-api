package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class InvalidScheduleIdException extends ResponseStatusException{
    public InvalidScheduleIdException(HttpStatusCode status, String message) {
        super(status, message, null);
    }
}
