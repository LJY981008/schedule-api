package com.example.scheduleapi.lv2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Schedule {
    @Setter
    private Long id;
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
