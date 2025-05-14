package com.example.scheduleapi.repository.port;

import com.example.scheduleapi.entity.User;

import java.util.Optional;

/**
 * 사용자 데이터 접근을 위한 Repository 인터페이스
 */
public interface UserRepository {

    void createUser(User user);

    Optional<Object> findUserAttributeById(String attributeName, Long userId);
}