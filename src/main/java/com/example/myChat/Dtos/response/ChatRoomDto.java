package com.example.myChat.Dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatRoomDto {
    private String roomId;
    private List<MessageDto> messages;
    private List<UserSummaryDto> participants;
    private String status;
    private LocalDateTime createdAt;
}