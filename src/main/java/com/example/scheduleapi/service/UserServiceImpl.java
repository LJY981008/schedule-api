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
    public void checkedSignup(ScheduleRequestDto dto) {
        User user = new User(dto.getUser_id(), dto.getPublisher(), dto.getEmail(), dto.getPassword());
        Optional<Object> user_id = userRepository.findUserByColumnKeyAndId("user_id", user.getUser_id());
        if (user_id.isEmpty()) {
            userRepository.saveUser(user);
        }
    }
}
