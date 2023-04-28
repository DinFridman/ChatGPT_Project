package com.example.chatgptproject.service;


import com.example.chatgptproject.dto.ChatMessageDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.example.chatgptproject.utils.Constants.SHARE_CONVERSATION_BY_EMAIL_RESPONSE;

@Log4j2
@RequiredArgsConstructor
@Service
public class ConversationSenderService {
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final TelegramUserStateService telegramUserStateService;

    public String handleSendConversationRequest(ChatMessageDTO chatMessageDTO) {
        Long chatId = chatMessageDTO.getChatId();
        if(isEmailConversationState(chatId)) {
            handleEmailConversationState(chatMessageDTO);
            return SHARE_CONVERSATION_BY_EMAIL_RESPONSE;
        }

        startEmailConversationState(chatId);
        return "Please provide an email to send your conversation.";
    }

    private Boolean isEmailConversationState(Long chatId) {
        return telegramUserStateService.checkIfEmailConversationStateOn(chatId);
    }

    private void handleEmailConversationState(ChatMessageDTO chatMessageDTO) {
        Long chatId = chatMessageDTO.getChatId();
        //String recipient = getEmailFromUsersMapByChatId(chatId);
        String recipient = chatMessageDTO.getMessage();
        telegramRequestServiceImpl.handleShareConversationRequest(chatMessageDTO, recipient);
        turnOffEmailConversationState(chatId);
    }

    private void turnOffEmailConversationState(Long chatId) {
        telegramUserStateService.turnOffEmailConversationState(chatId);
    }

    /*private String getEmailFromUsersMapByChatId(Long chatId) {
        return telegramUserStateService.getLoginUserDTOFromUsersMap(chatId).getEmail();
    }*/

    private void startEmailConversationState(Long chatId) {
        telegramUserStateService.turnOnEmailConversationState(chatId);
    }

}
