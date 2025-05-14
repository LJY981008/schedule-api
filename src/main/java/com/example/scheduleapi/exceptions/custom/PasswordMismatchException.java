package com.example.scheduleapi.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * 비밀번호 불일치 시 발생하는 예외 클래스
 */
@Getter
public class PasswordMismatchException extends ResponseStatusException {

    private final String message;
    private final HttpStatusCode status;

    public PasswordMismatchException(String message) {
        super(HttpStatus.BAD_REQUEST, message, null);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}