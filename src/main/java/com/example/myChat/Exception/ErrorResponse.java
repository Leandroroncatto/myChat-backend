package com.example.myChat.Exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonPropertyOrder({"message", "statusCode", "details"})
public class ErrorResponse {

    private final String message;
    private final HttpStatus statusCode;
    private Map<String, Object> details = new HashMap<>();

    public ErrorResponse(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse(HttpStatus statusCode, String message, Map<String, Object> details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }
}