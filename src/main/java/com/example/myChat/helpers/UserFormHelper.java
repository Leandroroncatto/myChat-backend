package com.example.myChat.helpers;

import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Dtos.request.UpdateUserRequestDto;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserFormHelper {

    private final UserRepository userRepository;

    public UserFormHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> validateRegister(RegisterRequestDto request) {
        Map<String, String> errors = new HashMap<>();

        errors.putAll(validateBaseFields(request.getUsername(), request.getEmail(), request.getPassword(), request.getDisplayName()));
        errors.putAll(validateEmail(request.getEmail(), Optional.empty()));
        errors.putAll(validateUsername(request.getUsername(), Optional.empty()));
        errors.putAll(validatePassword(request.getPassword()));

        return errors;
    }

    public Map<String, String> validateUpdate(UUID id, UpdateUserRequestDto request) {
        Map<String, String> errors = new HashMap<>();

        request.getUsername().ifPresent(username -> errors.putAll(validateUsername(username, Optional.of(id))));
        request.getEmail().ifPresent(email -> errors.putAll(validateEmail(email, Optional.of(id))));
        request.getPassword().ifPresent(password -> errors.putAll(validatePassword(password)));

        return errors;
    }

    public Map<String, String> validateBaseFields(String username, String email, String password, String displayName) {
        Map<String, String> errors = new HashMap<>();
        if (username == null || username.isBlank()) errors.put("username", "Username is required");
        if (email == null || email.isBlank()) errors.put("email", "Email is required");
        if (password == null || password.isBlank()) errors.put("password", "Password is required");
        if (displayName == null || displayName.isBlank()) errors.put("displayName", "DisplayName is required");
        return errors;
    }

    public Map<String, String> validateUsername(String username, Optional<UUID> id) {
        Map<String, String> errors = new HashMap<>();
        Optional<User> userByUsername = userRepository.findByUsername(username);

        if (!username.matches("^[A-Za-z0-9._]+$")) {
            errors.put("username", "Username contains invalid characters");
        }

        if (username.length() < 3 || username.length() > 21) {
            errors.put("username", "Username must be between 3 and 21 characters");
        }

        if (userByUsername.isPresent()) {
            if (id.isEmpty() || !userByUsername.get().getId().equals(id.get())) {
                errors.put("username", "This username is already taken");
            }
        }

        return errors;
    }

    public Map<String, String> validateEmail(String email, Optional<UUID> id) {
        Map<String, String> errors = new HashMap<>();
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (!email.matches(emailRegex)) {
            errors.put("email", "Invalid email format");
            return errors;
        }


        if (userByEmail.isPresent()) {
            if (id.isEmpty() || !userByEmail.get().getId().equals(id.get())) {
                errors.put("email", "Email is already registered");
            }
        }

        return errors;
    }


    public Map<String, String> validatePassword(String password) {
        Map<String, String> errors = new HashMap<>();

        if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters long");
        }

        return errors;
    }


}