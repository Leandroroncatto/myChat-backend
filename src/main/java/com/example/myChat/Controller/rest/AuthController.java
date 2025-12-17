package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.request.LoginRequestDto;
import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Dtos.response.LoginResponseDto;
import com.example.myChat.Dtos.response.RegisterResponseDto;
import com.example.myChat.Model.User;
import com.example.myChat.Service.JwtService;
import com.example.myChat.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        authService.registerUser(registerRequest);
        RegisterResponseDto messageResponseDto = mountMessageResponseDto("Registration successful. Please verify the registered email within 20 minutes to activate your account.", Instant.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = authService.loginUser(loginRequest);
        String token = jwtService.generateToken(user.getId());
        LoginResponseDto authResponseDto = mountLoginResponseDto(token);

        return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }

    @GetMapping("/verify")
    public ResponseEntity<RegisterResponseDto> verifyEmail(@RequestParam("token") String verificationToken) {
        authService.verifyUser(verificationToken);
        RegisterResponseDto messageResponseDto = mountMessageResponseDto("Account active successfully", Instant.now());
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    private LoginResponseDto mountLoginResponseDto(String token) {
        LoginResponseDto authResponseDto = new LoginResponseDto();
        authResponseDto.setToken(token);
        authResponseDto.setExpiresAt(jwtService.extractExpirationInstant(token));

        return authResponseDto;
    }

    private RegisterResponseDto mountMessageResponseDto(String message, Instant timestamp) {
        RegisterResponseDto messageResponseDto = new RegisterResponseDto();
        messageResponseDto.setMessage(message);
        messageResponseDto.setTimestamp(timestamp);
        return messageResponseDto;
    }
}
