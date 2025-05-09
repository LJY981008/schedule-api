package com.example.scheduleapi.lv1.service;

import com.example.scheduleapi.lv1.dto.ScheduleRequestDto;
import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;
import com.example.scheduleapi.lv1.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(),
                dto.getPassword(),
                dto.getTitle(),
                dto.getContents()
        );
        scheduleRepository.saveSchedule(schedule);
        return makeResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto findScheduleByPublisher(String publisher) {
        return null;
    }
    private ScheduleResponseDto makeResponseDto(Schedule schedule){
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getPublisher(),
                schedule.getPassword(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getUpdatedDate()
        );
    }
}
