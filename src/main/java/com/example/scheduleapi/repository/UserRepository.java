package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.User;

import java.util.Optional;

public interface UserRepository {
    void saveUser(User user);

    Optional<Object> findUserByColumnKeyAndId(String key, Long user_id);
}
