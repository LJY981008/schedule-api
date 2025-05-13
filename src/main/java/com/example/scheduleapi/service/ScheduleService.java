package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate);

    List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size);

    ScheduleResponseDto findScheduleByUserId(Long userId);

    void updateScheduleById(ScheduleRequestDto requestDto, Long scheduleId);

    void deleteScheduleById(Long scheduleId);
}
