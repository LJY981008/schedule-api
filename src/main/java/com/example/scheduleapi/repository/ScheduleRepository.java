package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScheduleRepository {
    void saveSchedule(Schedule schedule, Long user_id);

    List<Schedule> filterSchedulesByPublisherAndDate(Long user_id, LocalDate startDate, LocalDate endDate);

    Schedule findScheduleByIdOrElseThrow(Long user_id);

    void updateScheduleOrElseThrow(Map<String, Object> scheduleMap, Long id);

    Object findScheduleByColumnKeyAndIdOrElseThrow(String key, Long id);

    Optional<Object> findUserByColumnKeyAndId(String key, Long user_id);

    int deleteSchedule(Long id);

    void saveUser(User user);
}
