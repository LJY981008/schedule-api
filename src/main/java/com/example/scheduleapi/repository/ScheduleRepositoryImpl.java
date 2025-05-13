package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.exceptions.InvalidScheduleIdException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * {@link ScheduleRepository} 인터페이스의 구현체 JDBC를 사용하여 스케줄 데이터를 관리
 */
@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 주어진 스케줄 정보를 데이터베이스에 생성하고, 생성된 ID를 스케줄 Entity에 설정
     * </p>
     *
     * @param schedule 생성할 스케줄 Entity
     * @param userId   스케줄을 생성하는 사용자의 ID
     */
    @Override
    public void createSchedule(Schedule schedule, Long userId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("post").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = makeParameters(schedule, userId);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        schedule.setScheduleId(key.longValue());
        schedule.setUpdatedDate(getUpdatedDate(key.longValue()).toString());
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 사용자의 스케줄 목록을 지정된 날짜 범위 내에서 조회
     * </p>
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜
     * @param endDate   조회 종료 날짜
     * @return 조회된 스케줄 Entity 리스트
     */
    @Override
    public List<Schedule> findSchedulesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM post WHERE user_id = ? AND updated_date >= ? AND updated_date <= ?";
        return jdbcTemplate.query(sql, scheduleRowMapper(), userId, Date.valueOf(startDate), Date.valueOf(endDate));
    }

    /**
     * {@inheritDoc}
     * <p>
     * 스케줄 목록을 페이지별로 조회
     * </p>
     *
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 한 페이지당 보여줄 스케줄 개수
     * @return 조회된 스케줄 Entity 리스트
     */
    @Override
    public List<Schedule> findSchedulesByPage(Long page, Long size) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM post ORDER BY id LIMIT ? OFFSET ?", scheduleRowMapper(), size, page * size);
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 유저 ID로 스케줄 정보를 조회하고, 결과가 없으면 예외 실행
     * </p>
     *
     * @param userId 조회할 스케줄의 유저 ID
     * @return 조회된 스케줄 엔티티
     * @throws InvalidScheduleIdException 해당 유저 ID의 스케줄이 없을 경우 실행
     */
    @Override
    public Schedule findScheduleById(Long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ?";
        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), userId);
        return queryOrElseThrow(result, userId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 ID의 스케줄 정보에서 주어진 속성의 값을 조회
     * </p>
     *
     * @param attributeName 조회할 속성의 이름
     * @param scheduleId  조회할 스케줄의 ID
     * @return 조회된 속성 값
     * @throws InvalidScheduleIdException 해당 ID의 스케줄이 없을 경우 실행
     */
    //TODO 컬럼명을 직접적으로 삽입하는 것은 보안에 취약하므로 검수과정 추가가 필요하다
    @Override
    public Object findScheduleByAttributeAndId(String attributeName, Long scheduleId) {
        String sql = "SELECT " + attributeName + " FROM post WHERE id = ?";
        List<Object> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString(1),
                scheduleId
        );
        return queryOrElseThrow(result, scheduleId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 ID의 스케줄 정보를 업데이트
     * </p>
     *
     * @param scheduleMap 업데이트할 속성-값 Map
     * @param scheduleId  업데이트할 스케줄의 ID
     * @throws InvalidScheduleIdException 해당 ID의 스케줄이 없을 경우 실행
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * 특정 ID의 스케줄 정보를 삭제
     * </p>
     *
     * @param scheduleId 삭제할 스케줄의 ID
     * @throws InvalidScheduleIdException 해당 ID의 스케줄이 없을 경우 실행
     */
    @Override
    public void deleteScheduleById(Long scheduleId) {
        int rowResult = jdbcTemplate.update("delete from post where id = ?", scheduleId);
        updateOrElseThrow(rowResult, scheduleId);
    }

    /**
     * 스케줄 정보를 생성하기 위한 파라미터 Map을 실행
     *
     * @param schedule 생성할 스케줄 Entity
     * @param userId   스케줄을 생성하는 사용자의 ID
     * @return 생성된 파라미터 Map
     */
    private Map<String, Object> makeParameters(Schedule schedule, Long userId) {
        return Map.of("userId", userId, "publisher", schedule.getPublisher(), "password", schedule.getPassword(), "title", schedule.getTitle(), "contents", schedule.getContents(), "updated_date", LocalDate.now());
    }

    /**
     * 특정 ID의 스케줄 updated_date를 조회
     *
     * @param scheduleId 조회할 스케줄의 ID
     * @return 조회된 updated_date
     */
    private LocalDate getUpdatedDate(Long scheduleId) {
        String sql = "SELECT updated_date FROM post WHERE id = ?";
        return LocalDate.from(jdbcTemplate.queryForObject(sql, LocalDateTime.class, scheduleId));
    }

    /**
     * 조회 결과 리스트의 첫 요소를 반환하고, 리스트가 비어있는 경우 예외를 실행
     *
     * @param <T>        리스트의 요소 타입
     * @param resultList 조회 결과 리스트
     * @param scheduleId 조회 대상 스케줄 ID
     * @return 리스트의 첫 번째 요소 (존재하는 경우)
     * @throws InvalidScheduleIdException 조회 결과가 없을 경우 실행
     */
    private <T> T queryOrElseThrow(List<T> resultList, Long scheduleId) {
        return resultList.stream()
                .findAny()
                .orElseThrow(() -> new InvalidScheduleIdException(HttpStatus.NOT_FOUND, "not found schedule ID: " + scheduleId));
    }

    /**
     * 데이터 갱신한 후 결과를 확인한 후
     * 갱신된 데이터가 없는 경우 예외를 실행
     *
     * @param rowResult  업데이트 또는 삭제 작업으로 영향을 받은 행 수
     * @param scheduleId 작업 대상 스케줄 ID
     * @throws InvalidScheduleIdException 작업 대상 스케줄이 없을 경우 실행
     */
    private void updateOrElseThrow(int rowResult, Long scheduleId) {
        if (rowResult == 0)
            throw new InvalidScheduleIdException(HttpStatus.NOT_FOUND, "not found schedule ID: " + scheduleId);
    }

    /**
     * 데이터베이스 조회 결과를 {@link Schedule} 엔티티로 매핑하는 {@link RowMapper} 구현체입니다.
     *
     * @return {@link Schedule} 타입의 {@link RowMapper}
     */
    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(rs.getLong("id"), rs.getLong("user_id"), rs.getString("publisher"), rs.getString("password"), rs.getString("title"), rs.getString("contents"), rs.getString("updated_date"));
            }
        };
    }
}