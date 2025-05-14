package com.example.scheduleapi.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 유효성 검사 실패 시 발생하는 예외 클래스
 */
@Getter
public class ValidationException extends ResponseStatusException {

    private final Map<String, String> errors;
    private final HttpStatusCode status;

    public ValidationException(Map<String, String> errors, String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errors = errors;
        this.status = HttpStatus.BAD_REQUEST;
    }
}