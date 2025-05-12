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
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(), dto.getPassword(), dto.getTitle(), dto.getContents());
        scheduleRepository.saveSchedule(schedule);
        return makeResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> filterSchedulesByPublisherAndDate(String publisher, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 요청 파라미터 형식에 맞춰 변경
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleRepository.filterSchedulesByPublisherAndDate(publisher, parsedStartDate, parsedEndDate);

        List<ScheduleResponseDto> responseDto = new ArrayList<>();
        for (Schedule item : scheduleList) {
            responseDto.add(makeResponseDto(item));
        }
        responseDto.sort(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed());
        return responseDto;
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        return makeResponseDto(scheduleRepository.findScheduleByIdOrElseThrow(id));
    }

    @Override
    public void updateSchedule(ScheduleRequestDto requestDto, Long id) {
        if(!requestDto.getPassword().equals(scheduleRepository.getDataOrElseThrow("password", id)))
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST, "password가 일치하지 않습니다.");
        Map<String, Object> validRequestMap = validRequestSetDefault(requestDto, id);
        scheduleRepository.updateScheduleOrElseThrow(validRequestMap, id);
    }

    private ScheduleResponseDto makeResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }
    private Map<String, Object> validRequestSetDefault(ScheduleRequestDto requestDto, Long id) {
        Map<String, Object> finalData = new HashMap<>(validRequestToMap(requestDto));

        String[] fields = {"publisher", "contents"};

        for (String field : fields) {
            if (!finalData.containsKey(field)) {
                Object defaultValue = scheduleRepository.getDataOrElseThrow(field, id);
                if (defaultValue != null) {
                    finalData.put(field, defaultValue);
                }
            }
        }

        return finalData;
    }
    private Map<String, Object> validRequestToMap(ScheduleRequestDto requestDto){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dtoMap = objectMapper.convertValue(requestDto, Map.class);

        return dtoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
