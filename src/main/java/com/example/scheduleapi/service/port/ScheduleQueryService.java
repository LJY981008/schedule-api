package com.example.scheduleapi.service.port;

import com.example.scheduleapi.dto.ResponseDto;

import java.util.List;

/**
 * 스케줄 관련 비즈니스 로직(SELECT)을 위한 Service 인터페이스
 */
public interface ScheduleQueryService {

    List<ResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate);

    List<ResponseDto> findSchedulesByPage(Long page, Long size);

    ResponseDto findScheduleByScheduleId(Long userId);
}
