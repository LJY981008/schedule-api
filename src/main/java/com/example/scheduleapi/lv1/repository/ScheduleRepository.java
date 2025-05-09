package com.example.scheduleapi.lv1.repository;

import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);
}
