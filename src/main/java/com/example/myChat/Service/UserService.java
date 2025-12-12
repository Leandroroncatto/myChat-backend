package com.example.myChat.Service;

import com.example.myChat.Exception.NotFound;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(String search) {
        List<User> users = userRepository.findAll();
        if (!search.isBlank()) {
            return userRepository.findByUsernameContainingIgnoreCase(search);
        }

        return users;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound("User not found", id));
    }
}
