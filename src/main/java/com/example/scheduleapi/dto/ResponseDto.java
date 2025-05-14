package com.example.scheduleapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 스케줄 정보를 응답으로 반환할 때 사용되는 DTO 클래스
 */
@Getter
@AllArgsConstructor
public class ResponseDto {
    private Long scheduleId;
    private String publisher;
    private String password;
    private String title;
    private String contents;
    private String updatedDate;
}
