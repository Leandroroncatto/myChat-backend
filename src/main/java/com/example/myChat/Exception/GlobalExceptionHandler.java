package com.example.myChat.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorResponse> unauthorized(Unauthorized ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = new ErrorResponse(
                status,
                ex.getMessage()
        );
        Map<String, Object> details = new HashMap<>();
        details.put("code", String.valueOf(status.value()));
        details.put("path", path);
        errorResponse.setDetails(details);


        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFound ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(
                status,
                ex.getMessage()
        );
        Map<String, Object> details = new HashMap<>();
        details.put("code", String.valueOf(status.value()));
        details.put("path", path);
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequest ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                status,
                ex.getMessage()
        );
        Map<String, Object> details = new HashMap<>();
        details.put("code", String.valueOf(status.value()));
        details.put("path", path);
        details.put("fields", ex.getFields());
        errorResponse.setDetails(details);

        return new ResponseEntity<>(errorResponse, status);
    }
}
