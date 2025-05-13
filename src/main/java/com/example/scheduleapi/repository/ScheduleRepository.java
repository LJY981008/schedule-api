package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository {
    void saveSchedule(Schedule schedule, Long user_id);

    List<Schedule> filterSchedulesByPublisherAndDate(Long user_id, LocalDate startDate, LocalDate endDate);

    Schedule findScheduleByIdOrElseThrow(Long user_id);

    void updateScheduleOrElseThrow(Map<String, Object> scheduleMap, Long id);

    Object findScheduleByColumnKeyAndIdOrElseThrow(String key, Long id);

    int deleteSchedule(Long id);

    List<Schedule> findSchedulesByPage(Long page, Long size);
}
