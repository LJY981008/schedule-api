package com.example.scheduleapi.lv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    private String updatedDate;
}
