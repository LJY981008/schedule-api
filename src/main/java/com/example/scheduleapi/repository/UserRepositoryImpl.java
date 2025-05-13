package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    @Override
    public void createUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = makeParameters(user);
        jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
    }

    private Map<String, Object> makeParameters(User user) {
        return Map.of("user_id", user.getUserId(), "name", user.getPublisher(), "email", user.getEmail(), "registration_date", LocalDate.now(), "modification_date", LocalDate.now());
    }
}
