package com.example.scheduleapi.service.port;

import com.example.scheduleapi.dto.ScheduleRequestDto;

/**
 * 사용자 관련 비즈니스 로직을 위한 Service 인터페이스
 */
public interface UserService {
    void registerUserIfNew(ScheduleRequestDto dto);
}