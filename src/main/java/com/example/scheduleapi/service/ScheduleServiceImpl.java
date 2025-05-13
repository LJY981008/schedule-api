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

/**
 * {@link ScheduleService} 인터페이스의 구현체로, 스케줄 관련 비즈니스 로직을 처리
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 새로운 스케줄을 생성하고 저장한 후, 응답 DTO를 반환
     *
     * @param dto 생성할 스케줄 정보를 담은 DTO
     * @return 생성된 스케줄 정보를 담은 {@link ScheduleResponseDto}
     */
    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getPublisher(), dto.getPassword(), dto.getTitle(), dto.getContents());
        scheduleRepository.createSchedule(schedule, dto.getUserId());
        return convertToResponseDto(schedule);
    }

    /**
     * 특정 사용자의 스케줄 목록을 지정된 날짜 범위 내에서 조회하고, 응답 DTO 목록으로 변환하여 반환
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate   조회 종료 날짜 (yyyy-MM-dd 형식)
     * @return 조회된 스케줄 목록을 담은 {@link ScheduleResponseDto} 리스트 (수정일 내림차순 정렬)
     */
    @Override
    public List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByUserAndDateRange(userId, parsedStartDate, parsedEndDate);

        return scheduleList.stream()
                .map(this::convertToResponseDto)
                .sorted(Comparator.comparing(ScheduleResponseDto::getUpdatedDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 특정 유저 ID로 스케줄 정보를 조회하고, 응답 DTO로 변환하여 반환
     *
     * @param userId 조회할 스케줄의 유저 ID
     * @return 조회된 스케줄 정보를 담은 {@link ScheduleResponseDto}
     */
    @Override
    public ScheduleResponseDto findScheduleByUserId(Long userId) {
        return convertToResponseDto(scheduleRepository.findScheduleById(userId));
    }

    /**
     * 특정 ID의 스케줄 정보를 수정
     * 비밀번호를 검증한 후, 요청 DTO의 정보를 기반으로 스케줄을 업데이트
     *
     * @param requestDto 수정할 스케줄 정보를 담은 DTO
     * @param scheduleId 수정할 스케줄의 ID
     * @throws PasswordMismatchException 비밀번호가 일치하지 않을 경우 발생
     */
    @Override
    public void updateScheduleById(ScheduleRequestDto requestDto, Long scheduleId) {
        validatePassword(scheduleId, requestDto.getPassword());
        Map<String, Object> validRequestMap = mergeUpdateRequest(requestDto, scheduleId);
        scheduleRepository.updateScheduleByIdOrElseThrow(validRequestMap, scheduleId);
    }

    /**
     * 특정 ID의 스케줄 정보를 삭제
     * 삭제 시 비밀번호를 검증
     *
     * @param scheduleId 삭제할 스케줄의 ID
     * @param password   삭제 요청 시 필요한 비밀번호
     * @throws PasswordMismatchException 비밀번호가 일치하지 않을 경우 발생
     */
    @Override
    public void deleteScheduleById(Long scheduleId, String password) {
        validatePassword(scheduleId, password);
        scheduleRepository.deleteScheduleById(scheduleId);
    }

    /**
     * 스케줄 목록을 페이지별로 조회하고, 응답 DTO 목록으로 변환하여 반환
     *
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 한 페이지당 보여줄 스케줄 개수
     * @return 조회된 스케줄 목록을 담은 {@link ScheduleResponseDto} 리스트
     */
    @Override
    public List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size) {
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByPage(page, size);
        return scheduleList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 데이터베이스에서 조회한 스케줄의 비밀번호와 요청된 비밀번호를 비교하여 검증
     *
     * @param scheduleId 검증할 스케줄의 ID
     * @param password   요청된 비밀번호
     * @throws PasswordMismatchException 비밀번호가 일치하지 않을 경우 발생
     */
    private void validatePassword(Long scheduleId, String password) {
        Object passwordFromDB = scheduleRepository.findScheduleByAttributeAndId("password", scheduleId);
        if (!password.equals(passwordFromDB)) {
            throw new PasswordMismatchException(HttpStatus.BAD_REQUEST, "password mismatch");
        }
    }

    /**
     * {@link Schedule} Entity를 {@link ScheduleResponseDto}로 변환
     *
     * @param schedule 변환할 스케줄 엔티티
     * @return 변환된 스케줄 응답 DTO
     */
    private ScheduleResponseDto convertToResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(schedule.getScheduleId(), schedule.getPublisher(), schedule.getPassword(), schedule.getTitle(), schedule.getContents(), schedule.getUpdatedDate());
    }

    /**
     * 업데이트 요청 DTO와 스케줄 ID를 기반으로 업데이트에 필요한 데이터를 병합합
     * 요청 DTO에 값이 없는 필드는 기존 스케줄의 값을 유지
     *
     * @param requestDto 업데이트 요청 정보를 담은 DTO
     * @param id         업데이트할 스케줄의 ID
     * @return 업데이트할 필드와 값을 담은 Map
     */
    private Map<String, Object> mergeUpdateRequest(ScheduleRequestDto requestDto, Long id) {
        Map<String, Object> finalData = new HashMap<>(convertRequestDtoToMap(requestDto));
        String[] fields = {"publisher", "contents"};
        for (String field : fields) {
            if (!finalData.containsKey(field)) {
                Object defaultValue = scheduleRepository.findScheduleByAttributeAndId(field, id);
                if (defaultValue != null) {
                    finalData.put(field, defaultValue);
                }
            }
        }
        return finalData;
    }

    /**
     * {@link ScheduleRequestDto}의 필드 중 null이 아닌 값들을 Map으로 변환
     *
     * @param requestDto 변환할 스케줄 요청 DTO
     * @return null이 아닌 필드와 값을 담은 Map
     */
    private Map<String, Object> convertRequestDtoToMap(ScheduleRequestDto requestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> dtoMap = objectMapper.convertValue(requestDto, Map.class);
        return dtoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}