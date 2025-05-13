package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 전역 예외 처리를 담당하는 클래스
 */
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * {@link ValidationException}이 발생했을 때 처리하는 메서드
     * 유효성 검사 실패 시 발생한 에러 정보를 담아 HTTP 상태 코드 400 (BAD_REQUEST)와 함께 응답
     *
     * @param ex 발생한 {@link ValidationException}
     * @return 에러 정보를 담은 Map과 HTTP 상태 코드를 포함하는 {@link ResponseEntity}
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }
}