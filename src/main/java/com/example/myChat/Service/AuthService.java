package com.example.myChat.Service;

import com.example.myChat.Dtos.request.LoginRequestDto;
import com.example.myChat.Dtos.request.RegisterRequestDto;
import com.example.myChat.Exception.BadRequest;
import com.example.myChat.Exception.NotFound;
import com.example.myChat.Exception.Unauthorized;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import com.example.myChat.helpers.LoginFormHelper;
import com.example.myChat.helpers.UserFormHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginFormHelper loginFormHelper;
    private final UserFormHelper userFormHelper;
    private final EmailSender emailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoginFormHelper loginFormHelper, UserFormHelper userFormHelper, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginFormHelper = loginFormHelper;
        this.userFormHelper = userFormHelper;
        this.emailSender = emailSender;

    }

    public User loginUser(LoginRequestDto request) {

        Map<String, String> formValidationErrors = loginFormHelper.validateLoginRequest(request);

        if (!formValidationErrors.isEmpty()) {
            throw new BadRequest("Invalid fields", formValidationErrors);
        }

        String identifier = request.getEmail() == null ? request.getUsername() : request.getEmail();
        User user = userRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new Unauthorized("Invalid Credentials"));

        if (!loginFormHelper.isPasswordCorrect(user.getPassword(), request.getPassword())) {
            throw new Unauthorized("Invalid Credentials");
        }

        if (!user.isEnabled()) {
            throw new Unauthorized("Account not activated. Please verify your email.");
        }

        return user;
    }

    public void registerUser(RegisterRequestDto request) {
        Map<String, String> errors = userFormHelper.validateRegister(request);

        if (!errors.isEmpty()) {
            throw new BadRequest("Invalid Fields", errors);
        }

        User user = buildUser(request);
        try {
            user.setEmailVerificationExpiresIn(generateTokenExpirationTime());
            userRepository.save(user);
            emailSender.sendVerificationEmail(user.getEmail(), user.getEmailVerificationToken());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequest("Something went wrong!");
        }
    }

    public void verifyUser(String emailVerificationToken) {
        User user = userRepository.findByEmailVerificationToken(emailVerificationToken)
                .orElseThrow(() -> new BadRequest("Invalid or expired verification link."));

        if (user.getEmailVerificationExpiresIn().isBefore(Instant.now())) {
            throw new BadRequest("Verification link expired");
        }

        user.setEnabled(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresIn(null);

        userRepository.save(user);
    }

    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFound("User not found"));

        if (user.isEnabled()) {
            throw new BadRequest("Account already verified.");
        }
        user.setEmailVerificationToken(generateVerificationToken());
        user.setEmailVerificationExpiresIn(generateTokenExpirationTime());
        userRepository.save(user);

        emailSender.sendVerificationEmail(user.getEmail(), user.getEmailVerificationToken());
    }

    public void forgotPasswordEmail(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setForgotPasswordToken(generateVerificationToken());
            user.setForgotPasswordTokenExpiresIn(generateTokenExpirationTime());
            userRepository.save(user);

            emailSender.sendForgotPasswordEmail(user.getEmail(), user.getForgotPasswordToken());
        }
    }

    public void resetPassword(String newPassword, String token) {
        User user = userRepository.findByForgotPasswordToken(token).orElseThrow(() -> new BadRequest("Invalid or expired token."));
        Map<String, String> errors = userFormHelper.validatePassword(newPassword);

        if (user.getForgotPasswordTokenExpiresIn().isBefore(Instant.now())) {
            throw new BadRequest("This token has expired");
        }

        if (!errors.isEmpty()) {
            throw new BadRequest("Invalid fields", errors);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setForgotPasswordToken(null);
        user.setForgotPasswordTokenExpiresIn(null);
        userRepository.save(user);
    }

    private User buildUser(RegisterRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setEmailVerificationToken(generateVerificationToken());
        return user;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private Instant generateTokenExpirationTime() {
        return Instant.now().plus(Duration.ofMinutes(20));
    }
}

