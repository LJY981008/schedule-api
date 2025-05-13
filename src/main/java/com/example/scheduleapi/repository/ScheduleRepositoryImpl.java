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
    public void saveSchedule(Schedule schedule, Long user_id) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = makeParameters(schedule, user_id);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setId(key.longValue());
        schedule.setUpdatedDate(getUpdatedDate(key.longValue()).toString());
    }

    @Override
    public List<Schedule> filterSchedulesByPublisherAndDate(Long user_id, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query("SELECT * FROM post WHERE user_id = ? AND updated_date >= ? AND updated_date <= ?", scheduleRowMapper(), user_id, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long user_id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM post WHERE user_id = ?", scheduleRowMapper(), user_id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notting id = " + user_id));
    }

    @Override
    public void updateScheduleOrElseThrow(Map<String, Object> scheduleMap, Long id) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE post SET publisher = ?, contents = ?, updated_date = ? WHERE id = ?",
                scheduleMap.get("publisher"),
                scheduleMap.get("contents"),
                LocalDate.now(),
                id
        );
        if (rowsAffected == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notting id = " + id);
    }

    @Override
    public Object findScheduleByColumnKeyAndIdOrElseThrow(String key, Long id) {
        String sql = "SELECT " + key + " FROM post WHERE id = ?";
        List<Object> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString(1),
                id
        );
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notting id = " + id));
    }

    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("delete from post where id = ?", id);
    }

    @Override
    public List<Schedule> findSchedulesByPage(Long page, Long size) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM post ORDER BY id LIMIT ? OFFSET ?", scheduleRowMapper(), size, page * size);
        return result;
    }

    private Map<String, Object> makeParameters(Schedule schedule, Long user_id) {
        return Map.of("user_id", user_id, "publisher", schedule.getPublisher(), "password", schedule.getPassword(), "title", schedule.getTitle(), "contents", schedule.getContents(), "updated_date", LocalDate.now());
    }

    private LocalDate getUpdatedDate(Long id) {
        String sql = "SELECT updated_date FROM post WHERE id = ?";
        return LocalDate.from(jdbcTemplate.queryForObject(sql, LocalDateTime.class, id));
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("id"), rs.getLong("user_id"), rs.getString("publisher"), rs.getString("password"), rs.getString("title"), rs.getString("contents"), rs.getString("updated_date"));
            }
        };
    }
}
