package com.example.myChat.Dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonPropertyOrder({"message", "timestamp"})
public class RegisterResponseDto {
    private String message;
    private Instant timestamp;
}
