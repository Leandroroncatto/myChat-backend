package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.request.LoginRequest;
import com.example.myChat.Dtos.request.RegisterRequest;
import com.example.myChat.Model.User;
import com.example.myChat.Service.JwtService;
import com.example.myChat.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        User user = authService.registerUser(registerRequest);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("TOKEN: " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = authService.loginUser(loginRequest);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("TOKEN: " + token);
    }
}
