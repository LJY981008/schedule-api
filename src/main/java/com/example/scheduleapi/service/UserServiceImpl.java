package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * {@link UserService} 인터페이스의 구현체로, 사용자 관련 비즈니스 로직을 처리
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 주어진 스케줄 요청 DTO에 포함된 사용자 ID를 사용하여 사용자를 조회하고,
     * 존재하지 않는 경우 새로운 사용자를 등록
     * </p>
     *
     * @param dto 사용자 등록 정보를 담고 있는 스케줄 요청 DTO
     */
    @Override
    public void registerUserIfNew(ScheduleRequestDto dto) {
        User user = new User(dto.getUserId(), dto.getPublisher(), dto.getEmail(), dto.getPassword());
        Optional<Object> existingUserId = userRepository.findUserAttributeByIdOrElseThrow("user_id", user.getUserId());
        if (existingUserId.isEmpty()) {
            userRepository.createUser(user);
        }
    }
}