package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.request.LoginRequestDto;
import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Dtos.request.ResendEmailVerificationDto;
import com.example.myChat.Dtos.response.LoginResponseDto;
import com.example.myChat.Dtos.response.MessageResponseDto;
import com.example.myChat.Model.User;
import com.example.myChat.Service.EmailSender;
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
    public ResponseEntity<MessageResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        authService.registerUser(registerRequest);
        MessageResponseDto messageResponseDto = mountMessageResponseDto("You’ve successfully registered. Verify your email within 20 minutes to activate your account.", Instant.now());

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
    public ResponseEntity<MessageResponseDto> verifyEmail(@RequestParam("token") String verificationToken) {
        authService.verifyUser(verificationToken);
        MessageResponseDto messageResponseDto = mountMessageResponseDto("Account active successfully", Instant.now());
        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<MessageResponseDto> resendEmailVerification(@RequestBody ResendEmailVerificationDto resendEmailVerificationDto) {
        authService.resendVerificationEmail(resendEmailVerificationDto.getEmail());
        MessageResponseDto messageResponseDto = mountMessageResponseDto("Verification email resent. Please check your inbox.", Instant.now());
        return ResponseEntity.ok().body(messageResponseDto);
    }

    private LoginResponseDto mountLoginResponseDto(String token) {
        LoginResponseDto authResponseDto = new LoginResponseDto();
        authResponseDto.setToken(token);
        authResponseDto.setExpiresAt(jwtService.extractExpirationInstant(token));

        return authResponseDto;
    }

    private MessageResponseDto mountMessageResponseDto(String message, Instant timestamp) {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setMessage(message);
        messageResponseDto.setTimestamp(timestamp);
        return messageResponseDto;
    }
}
