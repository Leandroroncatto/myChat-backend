package com.example.myChat.Service;

import com.example.myChat.Exception.NotFound;
import com.example.myChat.Exception.Unauthorized;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User getLoggedInUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new NotFound("User not found"));
    }


    public void deleteUser(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound("User not found"));

        if (!(user.getUsername().equals(username))) {
            throw new Unauthorized("You are not authorized to do this");
        }

        userRepository.delete(user);
    }
}
