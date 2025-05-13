package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.exceptions.InvalidUserIdException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link UserRepository} 인터페이스의 구현체
 * JDBC를 사용하여 사용자 데이터를 관리
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 특정 ID의 사용자 정보에서 주어진 속성의 값을 조회
     * </p>
     *
     * @param attributeName 조회할 속성의 이름
     * @param userId        조회할 사용자의 ID
     * @return 조회된 속성 값
     */
    @Override
    public Optional<Object> findUserAttributeById(String attributeName, Long userId) {
        List<Object> result = jdbcTemplate.query(
                "SELECT ? FROM user WHERE user_id = ?",
                (rs, rowNum) -> rs.getString(attributeName),
                attributeName,
                userId
        );
        return result.stream().findAny();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 주어진 사용자 정보를 데이터베이스에 생성
     * </p>
     *
     * @param user 생성할 사용자 Entity
     */
    @Override
    public void createUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = makeParameters(user);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setUserId(key.longValue());
    }

    /**
     * 사용자 정보를 기반으로 데이터베이스에 저장할 파라미터 Map을 생성
     *
     * @param user 사용자 엔티티
     * @return 생성된 파라미터 Map
     */
    private Map<String, Object> makeParameters(User user) {
        return Map.of("user_id", user.getUserId(), "name", user.getPublisher(), "email", user.getEmail(), "registration_date", LocalDate.now(), "modification_date", LocalDate.now());
    }
}