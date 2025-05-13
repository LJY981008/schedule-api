package com.example.scheduleapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보를 담는 Entity 클래스
 */
@Getter
@AllArgsConstructor
public class User {
    @Setter
    private Long userId;
    private String publisher;
    private String email;
    private String password;

    public boolean isUserId() {
        return userId != null;
    }
}
