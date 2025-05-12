package com.example.scheduleapi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class User {
    @Setter
    private Long user_id;
    private String publisher;
    private String email;
    private String password;

    public boolean isUserId(){
        return user_id != null;
    }
}
