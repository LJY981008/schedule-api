package com.example.scheduleapi.util;

import com.example.scheduleapi.entity.Schedule;
import com.example.scheduleapi.exceptions.custom.InvalidScheduleIdException;
import com.example.scheduleapi.exceptions.custom.PasswordMismatchException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Query 검증을 위한 유틸 클래스
 */
@Component
public class QueryValidator {

    public void validatePassword(Optional<Object> passwordFromDB, String password) {
        if (passwordFromDB.isEmpty()) {
            throw new InvalidScheduleIdException("schedule not found");
        } else if (!password.equals(passwordFromDB.get())) {
            throw new PasswordMismatchException("password mismatch");
        }
    }

    public Schedule validateSchedule(Optional<Schedule> schedule) {
        if (schedule.isEmpty()) throw new InvalidScheduleIdException("schedule not found");
        return schedule.get();
    }

    public Object validateObject(Optional<Object> object) {
        if (object.isEmpty()) throw new InvalidScheduleIdException("schedule not found");
        return object.get();
    }

    public void validateUpdate(int result) {
        if (result == 0) throw new InvalidScheduleIdException("schedule not found");
    }
}
