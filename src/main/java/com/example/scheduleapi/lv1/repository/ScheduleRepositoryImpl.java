package com.example.scheduleapi.lv1.repository;

import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = makeParameters(schedule);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return makeResponseDto(key, schedule);
    }

    private Map<String, Object> makeParameters(Schedule schedule){
        return Map.of(
                "publisher", schedule.getPublisher(),
                "password", schedule.getPassword(),
                "title", schedule.getTitle(),
                "contents", schedule.getContents(),
                "updated_date", Timestamp.valueOf(LocalDateTime.now())
        );
    }
    private ScheduleResponseDto makeResponseDto(Number key, Schedule schedule){
        LocalDateTime localDateTime = getUpdatedDate(key.longValue());
        return new ScheduleResponseDto(
                    key.longValue(),
                    schedule.getPublisher(),
                    schedule.getPassword(),
                    schedule.getTitle(),
                    schedule.getContents(),
                    localDateTime.toLocalDate().toString(),
                    localDateTime.toLocalTime().toString()
                );
    }
    private LocalDateTime getUpdatedDate(Long id){
        String sql =  "SELECT updated_date FROM post WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, LocalDateTime.class, id);
    }
}
