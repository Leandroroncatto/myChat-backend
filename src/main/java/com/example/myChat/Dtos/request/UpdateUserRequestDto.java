package com.example.myChat.Dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UpdateUserRequestDto {
    private Optional<String> username = Optional.empty();
    private Optional<String> displayName = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> bio = Optional.empty();
}
