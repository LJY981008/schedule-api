package com.example.scheduleapi.lv1.service;

import com.example.scheduleapi.lv1.dto.ScheduleRequestDto;
import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;
import com.example.scheduleapi.lv1.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

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
        return scheduleRepository.saveSchedule(schedule);
    }
}
