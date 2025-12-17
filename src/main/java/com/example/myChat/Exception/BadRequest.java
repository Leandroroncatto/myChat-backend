package com.example.myChat.Exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BadRequest extends RuntimeException {

    Map<String, String> fields = null;

    public BadRequest(String message) {
        super(message);
    }

    public BadRequest(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }
}
