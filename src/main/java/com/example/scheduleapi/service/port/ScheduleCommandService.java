package com.example.scheduleapi.service.port;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;

/**
 * 스케줄 관련 비즈니스 로직(UPDATE)을 위한 Service 인터페이스
 */
public interface ScheduleCommandService {

    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    void updateScheduleById(ScheduleRequestDto requestDto, Long scheduleId);

    void deleteScheduleById(Long scheduleId, String password);
}