package com.example.scheduleapi.lv1.repository;

import com.example.scheduleapi.lv1.dto.ScheduleResponseDto;
import com.example.scheduleapi.lv1.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = makeParameters(schedule);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setId(key.longValue());
        schedule.setUpdatedDate(getUpdatedDate(key.longValue()).toString());
    }

    @Override
    public Optional<Schedule> findScheduleByPublisher(String publisher) {
        return null;
    }

    private Map<String, Object> makeParameters(Schedule schedule){
        return Map.of(
                "publisher", schedule.getPublisher(),
                "password", schedule.getPassword(),
                "title", schedule.getTitle(),
                "contents", schedule.getContents(),
                "updated_date", LocalDate.now()
        );
    }

    private LocalDate getUpdatedDate(Long id){
        String sql =  "SELECT updated_date FROM post WHERE id = ?";
        return LocalDate.from(jdbcTemplate.queryForObject(sql, LocalDateTime.class, id));
    }
}
