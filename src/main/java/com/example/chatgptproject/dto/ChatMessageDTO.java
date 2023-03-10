package com.example.chatgptproject.dto;

import lombok.*;

@Builder
@Data
public class ChatMessageDTO {
    private final long updateId;
    private final long chatId;
    private final String message;
    private final long userId;
    private final String role;
}
