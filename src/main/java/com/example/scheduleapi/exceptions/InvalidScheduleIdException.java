package com.example.scheduleapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * 유효하지 않거나 존재하지 않는 스케줄 ID로 요청했을 때 발생하는 예외 클래스
 */
public class InvalidScheduleIdException extends ResponseStatusException {

    /**
     * {@code InvalidScheduleIdException}의 생성자
     *
     * @param status  HTTP 상태 코드
     * @param message 예외에 대한 상세 메시지
     */
    public InvalidScheduleIdException(HttpStatusCode status, String message) {
        super(status, message, null);
    }
}