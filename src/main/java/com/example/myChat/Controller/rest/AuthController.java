package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.request.LoginRequest;
import com.example.myChat.Dtos.request.RegisterRequest;
import com.example.myChat.Model.User;
import com.example.myChat.Service.JwtService;
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
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("TOKEN: " + token);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.loginUser(loginRequest);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("TOKEN: " + token);
    }
}
