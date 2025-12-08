package com.example.myChat.Service;

import com.example.myChat.Dtos.LoginRequest;
import com.example.myChat.Dtos.RegisterRequest;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

        String identifier = request.getEmail() == null ? request.getUsername() : request.getEmail();
        Optional<User> user = userRepository.findByUsernameOrEmail(identifier, identifier);

        return user;
    }

    public User registerUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return user;
    }

}
