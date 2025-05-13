package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.exceptions.PasswordMismatchException;
import com.example.scheduleapi.repository.ScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(), dto.getPassword(), dto.getTitle(), dto.getContents());

        scheduleRepository.createSchedule(schedule, dto.getUserId());
        return convertToResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 요청 파라미터 형식에 맞춰 변경
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByUserAndDateRange(userId, parsedStartDate, parsedEndDate);

        List<ScheduleResponseDto> responseDto = new ArrayList<>();
        for (Schedule item : scheduleList) {
            responseDto.add(convertToResponseDto(item));
        }
        responseDto.sort(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed());
        return responseDto;
    }

    @Override
    public ScheduleResponseDto findScheduleByUserId(Long userId) {
        return convertToResponseDto(scheduleRepository.findScheduleById(userId));
    }

    @Override
    public void updateScheduleById(ScheduleRequestDto requestDto, Long scheduleId) {
        String password = scheduleRepository.findScheduleByCattributeAndId("password", scheduleId).toString();
        if (!requestDto.getPassword().equals(password))
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        Map<String, Object> validRequestMap = mergeUpdateRequest(requestDto, scheduleId);
        scheduleRepository.updateScheduleByIdOrElseThrow(validRequestMap, scheduleId);
    }

    @Override
    public void deleteScheduleById(Long scheduleId) {
        scheduleRepository.deleteScheduleById(scheduleId);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size) {
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByPage(page, size);
        return scheduleList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private ScheduleResponseDto convertToResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getScheduleId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }

    private Map<String, Object> mergeUpdateRequest(ScheduleRequestDto requestDto, Long id) {
        Map<String, Object> finalData = new HashMap<>(convertRequestDtoToMap(requestDto));

        String[] fields = {"publisher", "contents"};

        for (String field : fields) {
            if (!finalData.containsKey(field)) {
                Object defaultValue = scheduleRepository.findScheduleByCattributeAndId(field, id);
                if (defaultValue != null) {
                    finalData.put(field, defaultValue);
                }
            }
        }
        return finalData;
    }

    private Map<String, Object> convertRequestDtoToMap(ScheduleRequestDto requestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dtoMap = objectMapper.convertValue(requestDto, Map.class);

        return dtoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
