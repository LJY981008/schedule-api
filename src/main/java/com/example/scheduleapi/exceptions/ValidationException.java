package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 유효성 검사 실패 시 발생하는 예외 클래스
 */
public class ValidationException extends ResponseStatusException {

    private final Map<String, String> errors;

    /**
     * {@code ValidationException}의 생성자
     *
     * @param errors  유효성 검사 실패로 발생한 에러 정보를 담은 Map (필드명 - 에러 메시지)
     * @param message 예외에 대한 상세 메시지
     */
    public ValidationException(Map<String, String> errors, String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errors = errors;
    }

    /**
     * 유효성 검사 실패 시 발생한 에러 정보를 담은 Map을 반환
     *
     * @return 유효성 검사 에러 정보 Map (필드명 - 에러 메시지)
     */
    public Map<String, String> getErrors() {
        return errors;
    }
}