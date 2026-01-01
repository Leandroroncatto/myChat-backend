package com.example.myChat.Dtos.response;

import java.security.Timestamp;
import java.util.UUID;

public class MessageDto {
    private UUID id;
    private UUID senderId;
    private String senderName;
    private String content;
    private Timestamp timestamp;
}
