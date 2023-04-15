package com.example.chatgptproject.dto.mapper;
import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.utils.Constants;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatMessageDTOMapper {
    public ChatMessageDTO mapToDTO(Update update,String username) {
        return ChatMessageDTO.builder()
                    .updateId(update.getUpdateId().longValue())
                            .chatId(update.getMessage().getChatId())
                                    .message(update.getMessage().getText())
                                            .chatId(update.getMessage().getChatId())
                                                    .username(username)
                                                            .role(Constants.USER_ROLE)
                .build();
    }
}
