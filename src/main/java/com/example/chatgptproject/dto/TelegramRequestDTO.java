package com.example.chatgptproject.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TelegramRequestDTO {
    private final long updateId;
    private final long chatId;
    private final String message;
    private final long userId;
    private final String role;
}
