package com.example.scheduleapi.lv1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    private String updatedDate;
    public Schedule(String publisher, String password, String title, String contents){
        this.publisher = publisher;
        this.password = password;
        this.title = title;
        this.contents = contents;
    }
}
