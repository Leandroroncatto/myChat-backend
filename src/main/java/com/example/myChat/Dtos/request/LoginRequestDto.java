package com.example.myChat.Dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String username;
    private String email;
    private String password;
}
