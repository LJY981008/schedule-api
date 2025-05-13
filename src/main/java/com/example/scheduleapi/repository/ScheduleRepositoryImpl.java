package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.exceptions.InvalidScheduleIdException;
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
    public void createSchedule(Schedule schedule, Long userId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = makeParameters(schedule, userId);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setScheduleId(key.longValue());
        schedule.setUpdatedDate(getUpdatedDate(key.longValue()).toString());
    }

    @Override
    public List<Schedule> findSchedulesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM post WHERE user_id = ? AND updated_date >= ? AND updated_date <= ?";
        return jdbcTemplate.query(sql, scheduleRowMapper(), userId, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @Override
    public List<Schedule> findSchedulesByPage(Long page, Long size) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM post ORDER BY id LIMIT ? OFFSET ?", scheduleRowMapper(), size, page * size);
        return result;
    }

    @Override
    public Schedule findScheduleById(Long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ?";
        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), userId);
        return queryOrElseThrow(result, userId);
    }

    @Override
    public Object findScheduleByCattributeAndId(String attributeName, Long scheduleId) {
        String sql = "SELECT " + attributeName + " FROM post WHERE id = ?";
        List<Object> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString(1),
                scheduleId
        );
        return queryOrElseThrow(result, scheduleId);
    }

    @Override
    public void updateScheduleByIdOrElseThrow(Map<String, Object> scheduleMap, Long scheduleId) {
        int rowResult = jdbcTemplate.update(
                "UPDATE post SET publisher = ?, contents = ?, updated_date = ? WHERE id = ?",
                scheduleMap.get("publisher"),
                scheduleMap.get("contents"),
                LocalDate.now(),
                scheduleId
        );
        updateOrElseThrow(rowResult, scheduleId);
    }

    @Override
    public void deleteScheduleById(Long scheduleId) {
        int rowResult = jdbcTemplate.update("delete from post where id = ?", scheduleId);
        updateOrElseThrow(rowResult, scheduleId);
    }

    private Map<String, Object> makeParameters(Schedule schedule, Long userId) {
        return Map.of("userId", userId, "publisher", schedule.getPublisher(), "password", schedule.getPassword(), "title", schedule.getTitle(), "contents", schedule.getContents(), "updated_date", LocalDate.now());
    }

    private LocalDate getUpdatedDate(Long scheduleId) {
        String sql = "SELECT updated_date FROM post WHERE id = ?";
        return LocalDate.from(jdbcTemplate.queryForObject(sql, LocalDateTime.class, scheduleId));
    }

    private <T> T queryOrElseThrow(List<T> resultList, Long scheduleId) {
        return resultList.stream()
                .findAny()
                .orElseThrow(() -> new InvalidScheduleIdException(HttpStatus.NOT_FOUND, "not found schedule ID" + scheduleId));
    }
    private void updateOrElseThrow(int rowResult, Long scheduleId) {
        if(rowResult == 0) throw new InvalidScheduleIdException(HttpStatus.NOT_FOUND, "not found schedule ID" + scheduleId);
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
