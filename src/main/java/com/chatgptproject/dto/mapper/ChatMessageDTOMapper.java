package com.chatgptproject.dto.mapper;
import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.utils.constants.Constants;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatMessageDTOMapper {
    public ChatMessageDTO mapToDTO(Update update,String username) {
        return ChatMessageDTO.builder()
                    .updateId(update.getUpdateId().longValue())
                            .chatId(update.getMessage().getChatId())
                                    .message(update.getMessage().getText())
                                                    .username(username)
                                                            .role(Constants.USER_ROLE)
                .build();
    }
}
