package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUserIfNew(ScheduleRequestDto dto) {
        User user = new User(dto.getUserId(), dto.getPublisher(), dto.getEmail(), dto.getPassword());
        Optional<Object> userId = userRepository.findUserAttributeById("user_id", user.getUserId());
        if (userId.isEmpty()) {
            userRepository.createUser(user);
        }
    }
}
