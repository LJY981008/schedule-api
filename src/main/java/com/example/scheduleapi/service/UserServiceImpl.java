package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.RequestDto;
import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.repository.port.UserRepository;
import com.example.scheduleapi.service.port.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * {@link UserService} 인터페이스의 구현체
 * 사용자 관련 비즈니스 로직을 처리
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registerUserIfNew(RequestDto dto) {
        User user = new User(dto.getUserId(), dto.getPublisher(), dto.getEmail(), dto.getPassword());
        Optional<Object> findUserId = userRepository.findUserAttributeById("user_id", user.getUserId());
        if (findUserId.isEmpty())
            userRepository.createUser(user);
    }
}