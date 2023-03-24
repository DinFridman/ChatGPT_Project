package com.example.chatgptproject.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramResponseDTO {
    private String message;
    private Long chatId;
}
