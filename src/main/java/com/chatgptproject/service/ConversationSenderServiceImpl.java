package com.chatgptproject.service;

import com.chatgptproject.dto.ChatMessageDTO;
import com.chatgptproject.service.telegramService.TelegramRequestServiceImpl;
import com.chatgptproject.service.telegramService.TelegramUserStateServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import static com.chatgptproject.utils.constants.TelegramResponseConstants.CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY;
import static com.chatgptproject.utils.constants.TelegramResponseConstants.TELEGRAM_ENTER_EMAIL_MESSAGE;

@Log4j2
@RequiredArgsConstructor
@Service
public class ConversationSenderServiceImpl implements ConversationSenderService{
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;

    @Override
    public String handleSendConversationRequest(ChatMessageDTO chatMessageDTO) {
        Long chatId = chatMessageDTO.getChatId();
        if(isEmailConversationState(chatId)) {
            handleEmailConversationState(chatMessageDTO);
            return CONVERSATION_SENT_BY_EMAIL_SUCCESSFULLY;
        }

        startEmailConversationState(chatId);
        return TELEGRAM_ENTER_EMAIL_MESSAGE;
    }

    private boolean isEmailConversationState(Long chatId) {
        return telegramUserStateServiceImpl.checkIfEmailConversationStateOn(chatId);
    }

    private void handleEmailConversationState(ChatMessageDTO chatMessageDTO) {
        Long chatId = chatMessageDTO.getChatId();
        //String recipient = getEmailFromUsersMapByChatId(chatId);
        String recipient = chatMessageDTO.getMessage();
        telegramRequestServiceImpl.handleShareConversationRequest(chatMessageDTO, recipient);
        turnOffEmailConversationState(chatId);
    }

    private void turnOffEmailConversationState(Long chatId) {
        telegramUserStateServiceImpl.turnOffEmailConversationState(chatId);
    }

    /*private String getEmailFromUsersMapByChatId(Long chatId) {
        return telegramUserStateService.getLoginUserDTOFromUsersMap(chatId).getEmail();
    }*/

    private void startEmailConversationState(Long chatId) {
        telegramUserStateServiceImpl.turnOnEmailConversationState(chatId);
    }

}
