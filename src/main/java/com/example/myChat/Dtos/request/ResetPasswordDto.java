package com.example.myChat.Dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String password;
    private String token;
}
