package com.example.scheduleapi.lv1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    private String updatedDate;
    private String updatedTime;
}
