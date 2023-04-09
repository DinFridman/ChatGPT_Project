package com.example.chatgptproject.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramResponseDTO {
    private String text;
    @JsonProperty("chat_id")
    private Long chatId;
}
