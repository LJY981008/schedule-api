package com.example.scheduleapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @NotBlank
    private String password;
    @NotNull
    private Long user_id;
    private String email;
    private String publisher;
    private String title;
    private String contents;

    @AssertTrue(message = "입력을 확인해주세요")
    public boolean isUpdatePostValid() {
        return publisher != null || contents != null;
    }


}
