package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;
import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.entity.User;
import com.example.scheduleapi.exceptions.PasswordMismatchException;
import com.example.scheduleapi.repository.ScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        scheduleRepository.saveSchedule(schedule, dto.getUser_id());
        return makeResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> filterSchedulesByUserIdAndDate(Long user_id, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 요청 파라미터 형식에 맞춰 변경
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleRepository.filterSchedulesByPublisherAndDate(user_id, parsedStartDate, parsedEndDate);

        List<ScheduleResponseDto> responseDto = new ArrayList<>();
        for (Schedule item : scheduleList) {
            responseDto.add(makeResponseDto(item));
        }
        responseDto.sort(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed());
        return responseDto;
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long user_id) {
        return makeResponseDto(scheduleRepository.findScheduleByIdOrElseThrow(user_id));
    }

    @Override
    public void updateSchedule(ScheduleRequestDto requestDto, Long id) {
        String password = scheduleRepository.findScheduleByColumnKeyAndIdOrElseThrow("password", id).toString();
        if(!requestDto.getPassword().equals(password))
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        Map<String, Object> validRequestMap = validRequestSetDefault(requestDto, id);
        scheduleRepository.updateScheduleOrElseThrow(validRequestMap, id);
    }

    @Override
    public void deleteSchedule(Long id) {
        int deletedRow = scheduleRepository.deleteSchedule(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size) {
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByPage(page, size);
        return scheduleList.stream()
                .map(this::makeResponseDto)
                .collect(Collectors.toList());
    }

    private ScheduleResponseDto makeResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }
    private Map<String, Object> validRequestSetDefault(ScheduleRequestDto requestDto, Long id) {
        Map<String, Object> finalData = new HashMap<>(validRequestToMap(requestDto));

        String[] fields = {"publisher", "contents"};

        for (String field : fields) {
            if (!finalData.containsKey(field)) {
                Object defaultValue = scheduleRepository.findScheduleByColumnKeyAndIdOrElseThrow(field, id);
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
