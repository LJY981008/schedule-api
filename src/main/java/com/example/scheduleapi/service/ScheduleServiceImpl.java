package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(), dto.getPassword(), dto.getTitle(), dto.getContents());
        scheduleRepository.saveSchedule(schedule);
        return makeResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> filterSchedulesByPublisherAndDate(String publisher, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 요청 파라미터 형식에 맞춰 변경
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleRepository.filterSchedulesByPublisherAndDate(publisher, parsedStartDate, parsedEndDate);

        List<ScheduleResponseDto> responseDto = new ArrayList<>();
        for (Schedule item : scheduleList) {
            responseDto.add(makeResponseDto(item));
        }
        responseDto.sort(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed());
        return responseDto;
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        return makeResponseDto(scheduleRepository.findScheduleByIdOrElseThrow(id));
    }

    private ScheduleResponseDto makeResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }
}
