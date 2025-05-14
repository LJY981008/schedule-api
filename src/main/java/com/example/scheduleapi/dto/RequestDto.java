package com.example.scheduleapi.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

/**
 * 스케줄 생성 및 수정 요청 시 사용되는 DTO 클래스
 */
@Getter
public class RequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotNull(message = "사용자 아이디를 입력해주세요.")
    private Long userId;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Size(max = 200, message = "최대 200자까지 입력가능합니다.")
    private String contents;

    private String publisher;
    private String title;

    @AssertTrue(message = "입력을 확인해주세요")
    public boolean isUpdatePostValid() {
        return publisher != null || contents != null;
    }
}
