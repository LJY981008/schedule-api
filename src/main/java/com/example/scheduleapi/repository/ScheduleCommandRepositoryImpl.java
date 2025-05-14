package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.repository.port.ScheduleCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

/**
 * {@link ScheduleCommandRepository} 인터페이스의 구현체
 * JDBC를 사용하여 스케줄 데이터를 관리
 */
@RequiredArgsConstructor
@Repository
public class ScheduleCommandRepositoryImpl implements ScheduleCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createSchedule(Schedule schedule, Long userId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("user_id");

        LocalDate now = LocalDate.now();
        Map<String, Object> parameters = makeParameters(schedule, userId, now);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setScheduleId(key.longValue());
        schedule.setUpdatedDate(now.toString());
    }

    @Override
    public int updateScheduleById(Map<String, Object> scheduleMap, Long scheduleId) {
        return jdbcTemplate.update(
                "UPDATE post SET publisher = ?, contents = ?, updated_date = ? WHERE id = ?",
                scheduleMap.get("publisher"),
                scheduleMap.get("contents"),
                LocalDate.now(),
                scheduleId
        );
    }

    @Override
    public int deleteScheduleById(Long scheduleId) {
        return jdbcTemplate.update("delete from post where id = ?", scheduleId);
    }

    private Map<String, Object> makeParameters(Schedule schedule, Long userId, LocalDate updatedDate) {
        return Map.of("userId", userId, "publisher", schedule.getPublisher(), "password", schedule.getPassword(), "title", schedule.getTitle(), "contents", schedule.getContents(), "updated_date", updatedDate);
    }
}