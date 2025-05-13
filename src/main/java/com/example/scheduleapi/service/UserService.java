package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;

public interface UserService {
    void registerUserIfNew(ScheduleRequestDto dto);
}
