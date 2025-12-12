package com.example.myChat.Service;

import com.example.myChat.Dtos.request.LoginRequest;
import com.example.myChat.Dtos.request.RegisterRequest;
import com.example.myChat.Exception.BadRequest;
import com.example.myChat.Exception.Unauthorized;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import com.example.myChat.helpers.LoginFormHelper;
import com.example.myChat.helpers.RegisterFormHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginFormHelper loginFormHelper;
    private final RegisterFormHelper registerFormHelper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoginFormHelper loginFormHelper, RegisterFormHelper registerFormHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginFormHelper = loginFormHelper;
        this.registerFormHelper = registerFormHelper;
    }

    public User loginUser(LoginRequest request) {

        Map<String, String> formValidationErrors = loginFormHelper.validateLoginRequest(request);

        if (!formValidationErrors.isEmpty()) {
            throw new BadRequest("Invalid fields", formValidationErrors);
        }

        String identifier = request.getEmail() == null ? request.getUsername() : request.getEmail();
        User user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new Unauthorized("Invalid Credentials"));

        if (!loginFormHelper.isPasswordCorrect(user.getPassword(), request.getPassword())) {
            throw new Unauthorized("Invalid Credentials");
        }

        return user;
    }

    public User registerUser(RegisterRequest request) {
        Map<String, String> errors = registerFormHelper.validateRegisterRequest(request);

        if (!errors.isEmpty()) {
            throw new BadRequest("Invalid Fields", errors);
        }

        User user = buildUser(request);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequest("Something went wrong!");
        }
    }

    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }
}

