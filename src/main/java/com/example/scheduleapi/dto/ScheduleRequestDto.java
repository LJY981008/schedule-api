package com.example.scheduleapi.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String publisher;
    private String password;
    private String title;
    private String contents;
}
