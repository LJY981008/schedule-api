package com.example.scheduleapi.lv1.repository;

import com.example.scheduleapi.lv1.dto.ScheduleRequestDto;
import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;

import java.util.Optional;

public interface ScheduleRepository {
    void saveSchedule(Schedule schedule);
    Optional<Schedule> findScheduleByPublisher(String publisher);
}
