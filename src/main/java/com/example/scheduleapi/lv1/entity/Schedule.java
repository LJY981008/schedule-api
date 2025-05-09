package com.example.scheduleapi.lv1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long index;
    private String title;
    private String contents;
    private LocalDate updatedDate;
    private LocalDateTime updatedTime;
}
