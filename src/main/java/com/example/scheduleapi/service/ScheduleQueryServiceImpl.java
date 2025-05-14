package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.repository.port.ScheduleQueryRepository;
import com.example.scheduleapi.service.port.ScheduleQueryService;
import com.example.scheduleapi.util.QueryValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link ScheduleQueryService} 인터페이스의 구현체
 * 스케줄 관련 비즈니스 로직(SELECT)을 처리
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final QueryValidator queryValidator;
    private final ScheduleQueryRepository scheduleQueryRepository;

    @Override
    public List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleQueryRepository.findSchedulesByUserAndDateRange(userId, parsedStartDate, parsedEndDate);

        return scheduleList.stream()
                .map(this::convertToResponseDto)
                .sorted(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto findScheduleByScheduleId(Long scheduleId) {
        Schedule schedule = queryValidator.validateSchedule(scheduleQueryRepository.findScheduleById(scheduleId));
        return convertToResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size) {
        List<Schedule> scheduleList = scheduleQueryRepository.findSchedulesByPage(page, size);
        return scheduleList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private ScheduleResponseDto convertToResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getScheduleId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }
}
