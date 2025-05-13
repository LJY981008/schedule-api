package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.dto.ScheduleResponseDto;

import java.util.List;

/**
 * 스케줄 관련 비즈니스 로직을 제공하는 인터페이스
 */
public interface ScheduleService {

    /**
     * 새로운 스케줄을 생성하고 저장합
     *
     * @param dto 생성할 스케줄 정보를 담은 DTO
     * @return 생성된 스케줄 정보를 담은 Response DTO
     */
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    /**
     * 특정 사용자의 스케줄 목록을 지정된 날짜 범위 내에서 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate   조회 종료 날짜 (yyyy-MM-dd 형식)
     * @return 조회된 스케줄 목록을 담은 Response DTO 리스트
     */
    List<ScheduleResponseDto> findSchedulesByUserAndDateRange(Long userId, String startDate, String endDate);

    /**
     * 스케줄 목록을 페이지별로 조회
     *
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 한 페이지당 보여줄 스케줄 개수
     * @return 조회된 스케줄 목록을 담은 Response DTO 리스트
     */
    List<ScheduleResponseDto> findSchedulesByPage(Long page, Long size);

    /**
     * 특정 유저 ID로 스케줄 정보를 조회
     *
     * @param userId 조회할 스케줄의 유저 ID
     * @return 조회된 스케줄 정보를 담은 Response DTO
     */
    ScheduleResponseDto findScheduleByUserId(Long userId);

    /**
     * 특정 ID의 스케줄 정보를 수정
     *
     * @param requestDto 수정할 스케줄 정보를 담은 DTO
     * @param scheduleId 수정할 스케줄의 ID
     */
    void updateScheduleById(ScheduleRequestDto requestDto, Long scheduleId);

    /**
     * 특정 ID의 스케줄 정보를 삭제
     * 삭제 시 비밀번호를 확인
     *
     * @param scheduleId 삭제할 스케줄의 ID
     * @param password   삭제 요청 시 필요한 비밀번호
     */
    void deleteScheduleById(Long scheduleId, String password);
}