package com.example.myChat.Service;

import com.example.myChat.Dtos.request.LoginRequest;
import com.example.myChat.Dtos.request.RegisterRequest;
import com.example.myChat.Exception.BadRequest;
import com.example.myChat.Exception.Unauthorized;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> authenticateUser(LoginRequest request) {
        String identifier = request.getEmail() == null ? request.getUsername() : request.getEmail();

        return Optional.ofNullable(userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new Unauthorized("Invalid credentials")));
    }

    public Optional<User> registerUser(RegisterRequest request) {
        Map<String, String> errors = validateRequest(request);

        if (!errors.isEmpty()) {
            throw new BadRequest("Invalid form", errors);
        }

        User user = buildUser(request);

        try {
            User savedUser = userRepository.save(user);
            return Optional.of(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw handleDatabaseError(e);
        }
    }

    private Map<String, String> validateRequest(RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            errors.put("username", "Required field");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.put("email", "Required field");
        }
        if (request.getDisplayName() == null || request.getDisplayName().isBlank()) {
            errors.put("email", "Required field");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.put("password", "Required field");
        }
        return errors;
    }

    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

    private BadRequest handleDatabaseError(DataIntegrityViolationException e) {
        Map<String, String> errors = new HashMap<>();
        System.out.println(e.getMessage());

        return new BadRequest(e.getMessage());
    }
}
