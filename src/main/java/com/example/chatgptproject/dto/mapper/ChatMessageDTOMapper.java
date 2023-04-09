package com.example.chatgptproject.dto.mapper;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.utils.Constants;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatMessageDTOMapper {
    public ChatMessageDTO mapToDTO(Update update) {
        return ChatMessageDTO.builder()
                    .updateId(update.getUpdateId().longValue())
                            .chatId(update.getMessage().getChatId())
                                    .message(update.getMessage().getText())
                                            .chatId(update.getMessage().getChatId())
                                                    .userId(update.getMessage().getFrom().getId().longValue())
                                                            .role(Constants.USER_ROLE)
                .build();
    }
}
