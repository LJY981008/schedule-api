package com.example.scheduleapi.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * 유효하지 않은 사용자 ID로 요청했을 때 발생하는 예외 클래스
 */
@Getter
public class InvalidUserIdException extends ResponseStatusException {

    private final String message;
    private final HttpStatusCode status;

    public InvalidUserIdException(String message) {
        super(HttpStatus.NOT_FOUND, message, null);
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }
}