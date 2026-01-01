package com.example.myChat.Controller.rest;

import com.example.myChat.Dtos.response.ChatRoomDto;
import com.example.myChat.Service.AuthService;

import com.example.myChat.Service.ChatService;
import com.example.myChat.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;

    public ChatController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDto> chat(@PathVariable String roomId) {
        UUID userId = userService.getLoggedInUserInfo().getId();
        ChatRoomDto chatData = chatService.loadChatRoomDetails(userId, roomId);
        return ResponseEntity.ok(chatData);
    }
}