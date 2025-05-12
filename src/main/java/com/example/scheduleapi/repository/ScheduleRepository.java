package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {
    void saveSchedule(Schedule schedule);

    List<Schedule> filterSchedulesByPublisherAndDate(String publisher, LocalDate startDate, LocalDate endDate);

    Schedule findScheduleByIdOrElseThrow(Long id);

    void updateScheduleOrElseThrow(Map<String, Object> scheduleMap, Long id);

    Object getDataOrElseThrow(String key, Long id);

    int deleteSchedule(Long id);
}
