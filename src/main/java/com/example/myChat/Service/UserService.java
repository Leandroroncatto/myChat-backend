package com.example.myChat.Service;

import com.example.myChat.Dtos.request.UpdateUserRequestDto;
import com.example.myChat.Exception.BadRequest;
import com.example.myChat.Exception.NotFound;
import com.example.myChat.Exception.Unauthorized;
import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import com.example.myChat.helpers.UserFormHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFormHelper userFormHelper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserFormHelper userFormHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFormHelper = userFormHelper;
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

    public User updateUser(UUID id, UpdateUserRequestDto updates) {
        Map<String, String> errors = userFormHelper.validateUpdate(id, updates);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound("User not found"));
        boolean isUserOwner = validateUserOwnership(id, user.getUsername());

        if (!isUserOwner) throw new Unauthorized("You are not authorized to do this");

        if (!errors.isEmpty()) {
            throw new BadRequest("Invalid Form", errors);
        }


        updates.getUsername().ifPresent(user::setUsername);
        updates.getDisplayName().ifPresent(user::setDisplayName);
        updates.getEmail().ifPresent(user::setEmail);
        updates.getEmail().ifPresent(user::setEmail);
        updates.getPassword().ifPresent((rawPassword) -> {
            String hashed = passwordEncoder.encode(rawPassword);
            user.setPassword(hashed);
        });
        updates.getBio().ifPresent(user::setBio);

        userRepository.save(user);
        return user;
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound("User not found"));
        boolean isUserOwner = validateUserOwnership(id, user.getUsername());

        if (!isUserOwner) throw new Unauthorized("You are not authorized to do this");

        userRepository.delete(user);
    }

    private boolean validateUserOwnership(UUID id, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        return username.equals(loggedUsername);
    }
}
