package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.request.LoginRequestDto;
import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Dtos.response.AuthResponseDto;
import com.example.myChat.Dtos.response.UserResponseDto;
import com.example.myChat.Model.User;
import com.example.myChat.Service.JwtService;
import com.example.myChat.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        User user = authService.registerUser(registerRequest);
        String token = jwtService.generateToken(user.getId());
        AuthResponseDto authResponseDto = mountAuthResponseDto(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = authService.loginUser(loginRequest);
        String token = jwtService.generateToken(user.getId());
        AuthResponseDto authResponseDto = mountAuthResponseDto(token);

        return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }

    private AuthResponseDto mountAuthResponseDto(String token) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setToken(token);
        authResponseDto.setExpiresAt(jwtService.extractExpirationInstant(token));

        return authResponseDto;
    }
}
