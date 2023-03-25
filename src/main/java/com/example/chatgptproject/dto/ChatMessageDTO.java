package com.example.chatgptproject.dto;

import lombok.*;

@Builder
@Data
public class ChatMessageDTO {
    private final Long updateId;
    private final Long chatId;
    private final String message;
    private final Long userId;
    private final String role;

}
