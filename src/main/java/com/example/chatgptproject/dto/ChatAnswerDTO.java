package com.example.chatgptproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatAnswerDTO {
    private final String chatId;
    private final String message;
}
