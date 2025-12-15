package com.example.myChat.Dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonPropertyOrder({ "token", "expiresAt" })
public class AuthResponseDto {
    private String token;
    private Instant expiresAt;
}
