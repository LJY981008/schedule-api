package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    void saveSchedule(Schedule schedule);

    List<Schedule> filterSchedulesByPublisherAndDate(String publisher, LocalDate startDate, LocalDate endDate);

    Schedule findScheduleByIdOrElseThrow(Long id);
}
