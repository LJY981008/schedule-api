package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> filterSchedulesByPublisherAndDate(String publisher, String startDate, String endDate);

    ScheduleResponseDto findScheduleById(Long id);
}
