package com.example.chatgptproject.dto.mapper;
import com.example.chatgptproject.dto.TelegramRequestDTO;
import com.example.chatgptproject.utils.Constants;
import org.telegram.telegrambots.api.objects.Update;

public class TelegramRequestDTOMapper {
    public TelegramRequestDTO mapToDTO(Update update) {
        return TelegramRequestDTO.builder()
                    .updateId(update.getUpdateId())
                            .chatId(update.getMessage().getChatId())
                                    .message(update.getMessage().getText())
                                            .chatId(update.getMessage().getChatId())
                                                    .userId(update.getMessage().getFrom().getId())
                                                            .role(Constants.USER_ROLE)
                .build();
    }
}
