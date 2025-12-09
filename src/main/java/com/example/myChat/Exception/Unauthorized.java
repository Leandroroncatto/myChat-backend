package com.example.myChat.Exception;

import lombok.Getter;

@Getter
public class Unauthorized extends RuntimeException {
    public Unauthorized(String message) {
        super(message);
    }
}
