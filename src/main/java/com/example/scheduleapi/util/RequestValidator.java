package com.example.scheduleapi.util;

import com.example.scheduleapi.exceptions.custom.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Request 검증을 위한 유틸 클래스
 */
@Component
public class RequestValidator {
    public void requestValidateErrorToMap(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ValidationException(errors, "유효성 검사 실패");
        }
    }
}
