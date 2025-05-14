package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.repository.port.UserRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    //TODO 컬럼명을 직접적으로 삽입하는 것은 보안에 취약하므로 검수과정 추가가 필요하다
    @Override
    public Optional<Object> findUserAttributeById(String attributeName, Long userId) {
        String sql = "SELECT " + attributeName + " FROM user WHERE user_id = ?";
        List<Object> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString(attributeName),
                userId
        );
        return result.stream().findAny();
    }

    @Override
    public void createUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = makeParameters(user);
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setUserId(key.longValue());
    }

    private Map<String, Object> makeParameters(User user) {
        return Map.of("user_id", user.getUserId(), "name", user.getPublisher(), "email", user.getEmail(), "registration_date", LocalDate.now(), "modification_date", LocalDate.now());
    }
}