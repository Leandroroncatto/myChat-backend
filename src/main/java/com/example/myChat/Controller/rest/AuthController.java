package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.LoginRequest;
import com.example.myChat.Dtos.RegisterRequest;
import com.example.myChat.Model.User;
import com.example.myChat.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body("TOKEN");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.authenticateUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user + "token: " + "token2121o21o21o291");
    }
}
