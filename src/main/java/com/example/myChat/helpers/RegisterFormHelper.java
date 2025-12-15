package com.example.myChat.helpers;

import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegisterFormHelper {

    private final UserRepository userRepository;

    public RegisterFormHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> validateRegisterRequest(RegisterRequestDto request) {
        Map<String, String> errors = new HashMap<>();

        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();
        String displayName = request.getDisplayName();

        if (username == null || username.isBlank()) {
            errors.put("username", "Username is required");
        }
        if (email == null || email.isBlank()) {
            errors.put("email", "Email is required");
        }
        if (password == null || password.isBlank()) {
            errors.put("password", "Password is required");
        }
        if (displayName == null || displayName.isBlank()) {
            errors.put("displayName", "DisplayName is required");
        }

        if (!errors.isEmpty()) return errors;

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!email.matches(emailRegex)) {
            errors.put("email", "Invalid email format");
        }

        if (!username.matches("^[A-Za-z0-9._]+$")) {
            errors.put("username", "Username contains invalid characters");
        }

        if (username.length() < 3 || username.length() > 21) {
            errors.put("username", "Username must be between 3 and 21 characters");
        }

        if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters long");
        }

        if (errors.isEmpty()) {
            if (userRepository.findByUsername(username).isPresent()) {
                errors.put("username", "Username is already taken");
            }

            if (userRepository.findByEmail(email).isPresent()) {
                errors.put("email", "Email is already registered");
            }
        }

        return errors;
    }

}
