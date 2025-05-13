package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;

import java.util.Optional;

public interface UserRepository {
    void createUser(User user);

    Optional<Object> findUserAttributeById(String attributeName, Long userId);
}
