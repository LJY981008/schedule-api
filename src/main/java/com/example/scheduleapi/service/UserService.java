package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;

/**
 * 사용자 관련 비즈니스 로직을 제공하는 인터페이스
 */
public interface UserService {

    /**
     * 주어진 스케줄 요청 DTO에 포함된 사용자 정보가 DB에 존재하지 않으면 새로운 사용자를 등록
     *
     * @param dto 사용자 등록 정보를 담고 있는 스케줄 요청 DTO
     */
    void registerUserIfNew(ScheduleRequestDto dto);
}