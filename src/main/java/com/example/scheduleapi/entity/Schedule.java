package com.example.scheduleapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 스케줄 정보를 담는 Entity 클래스
 */
@Getter
@AllArgsConstructor
public class Schedule {
    @Setter
    private Long scheduleId;
    private Long userId;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    @Setter
    private String updatedDate;

    public Schedule(String publisher, String password, String title, String contents) {
        this.publisher = publisher;
        this.password = password;
        this.title = title;
        this.contents = contents;
    }
}
