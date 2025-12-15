package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.response.UserResponseDto;
import com.example.myChat.Model.User;
import com.example.myChat.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/rest/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(value = "search", required = false, defaultValue = "") String search) {
        List<User> users = userService.getAllUsers(search);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        UserResponseDto response = mountUserResponse(user);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getLoggedUserInfo() {
        User user = userService.getLoggedInUserInfo();
        UserResponseDto response = mountUserResponse(user);

        return ResponseEntity.ok().body(response);
    }

    /*

    public ResponseEntity<User> updateUser() {

    }

    public void deleteUser() {

    }

     */

    private UserResponseDto mountUserResponse(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setBio(user.getBio());
        userResponseDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setDisplayName(user.getDisplayName());

        return userResponseDto;
    }
}
