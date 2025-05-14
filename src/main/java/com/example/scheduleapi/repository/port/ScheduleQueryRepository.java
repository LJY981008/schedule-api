package com.example.scheduleapi.repository.port;

import com.example.scheduleapi.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleQueryRepository {

    List<Schedule> findSchedulesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findSchedulesByPage(Long page, Long size);

    Optional<Schedule> findScheduleById(Long userId);

    Optional<Object> findScheduleByAttributeAndId(String key, Long scheduleId);
}
