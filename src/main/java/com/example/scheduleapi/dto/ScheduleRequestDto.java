package com.example.scheduleapi.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotNull(message = "사용자 아이디를 입력해주세요.")
    private Long user_id;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    private String publisher;
    private String title;
    @Size(max = 200, message = "최대 200자까지 입력가능합니다.")
    private String contents;

    @AssertTrue(message = "입력을 확인해주세요")
    public boolean isUpdatePostValid() {
        return publisher != null || contents != null;
    }
}
