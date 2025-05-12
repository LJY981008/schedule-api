package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> filterSchedulesByUserIdAndDate(Long user_id, String startDate, String endDate);

    ScheduleResponseDto findScheduleById(Long user_id);

    void updateSchedule(ScheduleRequestDto requestDto, Long id);

    void deleteSchedule(Long id);
}
