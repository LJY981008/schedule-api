package com.example.scheduleapi.repository.port;

import com.example.scheduleapi.entity.Schedule;

import java.util.Map;

/**
 * 스케줄 데이터 접근을 위한 Repository 인터페이스
 */
public interface ScheduleCommandRepository {

    void createSchedule(Schedule schedule, Long userId);

    int updateScheduleById(Map<String, Object> scheduleMap, Long scheduleId);

    int deleteScheduleById(Long scheduleId);
}