package com.example.scheduleapi.service.port;

import com.example.scheduleapi.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleQueryService {

    List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate);

    List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size);

    ScheduleResponseDto findScheduleByScheduleId(Long userId);
}
