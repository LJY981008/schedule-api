package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.RequestDto;
import com.example.scheduleapi.dto.ResponseDto;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.repository.port.ScheduleCommandRepository;
import com.example.scheduleapi.repository.port.ScheduleQueryRepository;
import com.example.scheduleapi.service.port.ScheduleCommandService;
import com.example.scheduleapi.service.port.UserService;
import com.example.scheduleapi.util.QueryValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link ScheduleCommandService} 인터페이스의 구현체
 * 스케줄 관련 비즈니스 로직(UPDATE)을 처리
 */
@Service
@RequiredArgsConstructor
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final ScheduleQueryRepository scheduleQueryRepository;
    private final ScheduleCommandRepository scheduleCommandRepository;
    private final UserService userService;
    private final QueryValidator queryValidator;

    @Override
    @Transactional
    public ResponseDto createSchedule(RequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(), dto.getPassword(), dto.getTitle(), dto.getContents());
        userService.registerUserIfNew(dto);
        scheduleCommandRepository.createSchedule(schedule, dto.getUserId());
        return convertToResponseDto(schedule);
    }

    @Override
    @Transactional
    public void updateScheduleById(RequestDto requestDto, Long scheduleId) {
        Optional<Object> passwordFromDB = scheduleQueryRepository.findScheduleByAttributeAndId("password", scheduleId);
        queryValidator.validatePassword(passwordFromDB, requestDto.getPassword());

        Map<String, Object> validRequestMap = mergeUpdateRequest(requestDto, scheduleId);
        int result = scheduleCommandRepository.updateScheduleById(validRequestMap, scheduleId);
        queryValidator.validateUpdate(result);
    }

    @Override
    @Transactional
    public void deleteScheduleById(Long scheduleId, String password) {
        Optional<Object> passwordFromDB = scheduleQueryRepository.findScheduleByAttributeAndId("password", scheduleId);
        queryValidator.validatePassword(passwordFromDB, password);

        int result = scheduleCommandRepository.deleteScheduleById(scheduleId);
        queryValidator.validateUpdate(result);
    }

    private ResponseDto convertToResponseDto(Schedule schedule) {
        return new ResponseDto(schedule.getScheduleId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }

    private Map<String, Object> mergeUpdateRequest(RequestDto requestDto, Long scheduleId) {
        Map<String, Object> finalData = new HashMap<>(convertRequestDtoToMap(requestDto));
        String[] fields = {"publisher", "contents"};
        for (String field : fields) {
            if (!finalData.containsKey(field)) {
                Object defaultValue = queryValidator.validateObject(scheduleQueryRepository.findScheduleByAttributeAndId(field, scheduleId));
                finalData.put(field, defaultValue);
            }
        }
        return finalData;
    }

    private Map<String, Object> convertRequestDtoToMap(RequestDto requestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dtoMap = objectMapper.convertValue(requestDto, Map.class);
        return dtoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}