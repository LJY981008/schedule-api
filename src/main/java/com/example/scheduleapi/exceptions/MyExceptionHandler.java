package com.example.scheduleapi.exceptions;

import com.example.scheduleapi.exceptions.custom.InvalidScheduleIdException;
import com.example.scheduleapi.exceptions.custom.InvalidUserIdException;
import com.example.scheduleapi.exceptions.custom.PasswordMismatchException;
import com.example.scheduleapi.exceptions.custom.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 전역 예외 처리를 담당하는 클래스
 */
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getErrors(), ex.getStatus());
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<String> handleInvalidUserIdException(InvalidScheduleIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(InvalidScheduleIdException.class)
    public ResponseEntity<String> handleInvalidScheduleIdException(InvalidScheduleIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}