package com.example.myChat.Exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotFound extends RuntimeException {

    UUID identifier;

    public NotFound(String message) {
        super(message);
    }

    public NotFound(String message, UUID identifier) {
        super(message);
        this.identifier = identifier;
    }
}
