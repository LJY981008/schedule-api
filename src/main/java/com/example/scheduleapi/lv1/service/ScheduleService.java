package com.example.scheduleapi.lv1.service;

import com.example.scheduleapi.lv1.dto.ScheduleRequestDto;
import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> filterSchedulesByPublisherAndDate(String publisher, String startDate, String endDate);

    ScheduleResponseDto findScheduleById(Long id);
}
