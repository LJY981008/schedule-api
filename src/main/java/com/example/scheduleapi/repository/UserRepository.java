package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;

import java.util.Optional;

/**
 * 사용자 데이터 접근을 위한 Repository 인터페이스
 */
public interface UserRepository {

    /**
     * 새로운 사용자 정보를 데이터베이스에 생성
     *
     * @param user 생성할 사용자 Entity
     */
    void createUser(User user);

    /**
     * 특정 ID의 사용자 정보에서 주어진 속성의 값을 조회
     *
     * @param attributeName 조회할 속성의 이름
     * @param userId        조회할 사용자의 ID
     * @return 조회된 속성 값
     */
    Optional<Object> findUserAttributeById(String attributeName, Long userId);
}