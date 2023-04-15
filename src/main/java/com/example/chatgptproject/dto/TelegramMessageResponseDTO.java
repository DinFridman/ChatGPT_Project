package com.example.chatgptproject.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Data
@Builder
public class TelegramMessageResponseDTO implements TelegramResponse{
    private String text;
    @JsonProperty("chat_id")
    private Long chatId;
}
