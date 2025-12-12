package com.example.myChat.Controller.rest;

import com.example.myChat.Model.User;
import com.example.myChat.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok().body(user);
    }


    /*

    public ResponseEntity<User> updateUser() {

    }

    public void deleteUser() {

    }

     */

}
