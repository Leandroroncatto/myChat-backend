package com.example.myChat.Dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String displayName;
    private String email;
    private String password;
    private String bio;
}
