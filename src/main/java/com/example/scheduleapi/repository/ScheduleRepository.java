package com.example.scheduleapi.repository;

import com.example.scheduleapi.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 스케줄 데이터 접근을 위한 Repository 인터페이스
 */
public interface ScheduleRepository {

    /**
     * 새로운 스케줄 정보를 데이터베이스에 생성
     *
     * @param schedule 생성할 스케줄 엔티티
     * @param userId   스케줄을 생성하는 사용자의 ID
     */
    void createSchedule(Schedule schedule, Long userId);

    /**
     * 특정 사용자의 스케줄 목록을 지정된 날짜 범위 내에서 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param startDate 조회 시작 날짜
     * @param endDate   조회 종료 날짜
     * @return 조회된 스케줄 Entity 리스트
     */
    List<Schedule> findSchedulesByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 스케줄 목록을 페이지별로 조회
     *
     * @param page 조회할 페이지 번호 (0부터 시작)
     * @param size 한 페이지당 보여줄 스케줄 개수
     * @return 조회된 스케줄 엔티티 리스트
     */
    List<Schedule> findSchedulesByPage(Long page, Long size);

    /**
     * 특정 유저 ID로 스케줄 정보를 조회
     *
     * @param userId 조회할 스케줄의 유저 ID
     * @return 조회된 스케줄 Entity
     */
    Schedule findScheduleById(Long userId);

    /**
     * 특정 ID의 스케줄 정보에서 주어진 속성(컬럼)의 값을 조회
     *
     * @param key        조회할 속성의 이름
     * @param scheduleId 조회할 스케줄의 ID
     * @return 조회된 속성 값
     */
    Object findScheduleByAttributeAndId(String key, Long scheduleId);

    /**
     * 특정 ID의 스케줄 정보를 업데이트
     * 업데이트에 실패하면 예외 발생
     *
     * @param scheduleMap 업데이트할 속성-값 Map
     * @param scheduleId  업데이트할 스케줄의 ID
     */
    void updateScheduleByIdOrElseThrow(Map<String, Object> scheduleMap, Long scheduleId);

    /**
     * 특정 ID의 스케줄 정보를 삭제
     *
     * @param scheduleId 삭제할 스케줄의 ID
     */
    void deleteScheduleById(Long scheduleId);
}