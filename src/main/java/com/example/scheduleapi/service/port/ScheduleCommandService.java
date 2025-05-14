package com.example.scheduleapi.service.port;

import com.example.scheduleapi.dto.RequestDto;
import com.example.scheduleapi.dto.ResponseDto;

/**
 * 스케줄 관련 비즈니스 로직(UPDATE)을 위한 Service 인터페이스
 */
public interface ScheduleCommandService {

    ResponseDto createSchedule(RequestDto dto);

    void updateScheduleById(RequestDto requestDto, Long scheduleId);

    void deleteScheduleById(Long scheduleId, String password);
}