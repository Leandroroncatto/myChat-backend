package com.example.myChat.Dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonPropertyOrder({"id", "profilePictureUrl", "username", "email", "displayName", "bio"})
public class UserResponseDto {
    private UUID id;
    private String profilePictureUrl;
    private String username;
    private String email;
    private String displayName;
    private String bio;
}
