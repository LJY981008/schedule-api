package com.example.scheduleapi.service;

import com.example.scheduleapi.dto.ScheduleRequestDto;
import com.example.scheduleapi.entity.User;

public interface UserService {
    void checkedSignup(ScheduleRequestDto dto);
}
