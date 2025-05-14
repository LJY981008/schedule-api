package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.repository.port.ScheduleQueryRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * {@link ScheduleQueryRepository} 인터페이스의 구현체
 * JDBC를 사용하여 스케줄 데이터를 관리
 */
@Repository
@AllArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {

    private final JdbcTemplate jdbcTemplate;

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
    public Optional<Schedule> findScheduleById(Long scheduleId) {
        String sql = "SELECT * FROM post WHERE id = ?";
        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), scheduleId);
        return result.stream().findAny();
    }

    //TODO 컬럼명을 직접적으로 삽입하는 것은 보안에 취약하므로 검수과정 추가가 필요하다
    @Override
    public Optional<Object> findScheduleByAttributeAndId(String attributeName, Long scheduleId) {
        String sql = "SELECT " + attributeName + " FROM post WHERE id = ?";
        List<Object> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString(1),
                scheduleId
        );
        return result.stream().findAny();
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> new Schedule(rs.getLong("id"), rs.getLong("user_id"), rs.getString("publisher"), rs.getString("password"), rs.getString("title"), rs.getString("contents"), rs.getString("updated_date"));
    }
}
