package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public List<Schedule> filterSchedulesByPublisherAndDate(String publisher, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query("SELECT * FROM post WHERE publisher = ? AND updated_date >= ? AND updated_date <= ?", scheduleRowMapper(), publisher, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM post WHERE id = ?", scheduleRowMapper(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notting id = " + id));
    }

    private Map<String, Object> makeParameters(Schedule schedule) {
        return Map.of("publisher", schedule.getPublisher(), "password", schedule.getPassword(), "title", schedule.getTitle(), "contents", schedule.getContents(), "updated_date", LocalDate.now());
    }

    private LocalDate getUpdatedDate(Long id) {
        String sql = "SELECT updated_date FROM post WHERE id = ?";
        return LocalDate.from(jdbcTemplate.queryForObject(sql, LocalDateTime.class, id));
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("id"), rs.getString("publisher"), rs.getString("password"), rs.getString("title"), rs.getString("contents"), rs.getString("updated_date"));
            }
        };
    }
}
