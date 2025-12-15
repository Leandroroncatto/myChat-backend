package com.example.myChat.helpers;

import com.example.myChat.Dtos.request.LoginRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginFormHelper {

    private final PasswordEncoder passwordEncoder;

    public LoginFormHelper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> validateLoginRequest(LoginRequestDto request) {
        Map<String, String> errors = new HashMap<>();

        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        boolean isEmail = email != null && !email.isBlank();

        if ((email == null || email.isBlank()) && (username == null || username.isBlank())) {
            errors.put("user", "Username or Email is required");
        }
        if (password == null || password.isBlank()) {
            errors.put("password", "Password is required");
        }

        if (!errors.isEmpty()) return errors;

        if (!isEmail && (username.length() < 3 || username.length() > 21)) {
            errors.put("username", "Username must be between 3 and 21 characters");
        }

        if (isEmail && !email.matches(emailRegex)) {
            errors.put("email", "Invalid email format");
        }

        return errors;
    }

    public boolean isPasswordCorrect(String encoded, String rawPassword) {
        return passwordEncoder.matches(rawPassword, encoded);
    }
}
