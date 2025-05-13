package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {
    void createSchedule(Schedule schedule, Long userId);

    List<Schedule> findSchedulesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    Schedule findScheduleById(Long userId);

    Object findScheduleByCattributeAndId(String key, Long scheduleId);

    void updateScheduleByIdOrElseThrow(Map<String, Object> scheduleMap, Long scheduleId);

    void deleteScheduleById(Long scheduleId);

    List<Schedule> findSchedulesByPage(Long page, Long size);
}
