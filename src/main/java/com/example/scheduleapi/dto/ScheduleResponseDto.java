package com.example.scheduleapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long scheduleId;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    private String updatedDate;
}
