package com.example.scheduleapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @NotBlank
    private String password;
    private String publisher;
    private String title;
    private String contents;

    @AssertTrue(message = "publisher, title, contents 중 최소 1개의 데이터가 필요합니다.")
    public boolean isUpdatePostValid() {
        if (title != null) return false;
        return publisher != null || contents != null;
    }
}
