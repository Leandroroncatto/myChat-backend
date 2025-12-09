package com.example.myChat.Exception;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException {
    public UserNotFound(String message) {
        super(message);
    }
}
