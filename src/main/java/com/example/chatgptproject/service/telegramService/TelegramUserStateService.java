package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.dto.TelegramResponse;
import com.example.chatgptproject.model.UserSessionDetails;

public interface TelegramUserStateService {
    TelegramResponse startRegisterState(Long chatId);
    boolean isRegisterRequest(Long chatId);
    boolean isUserLoggedIn(Long chatId);
    UserSessionDetails getUserSessionDetails(Long chatId);
    TelegramResponse startLoginState(Long chatId);
    boolean isUsernameProvided(Long chatId);
    void setUsernameToUserSession(String username, Long chatId);
    void setPasswordToUserSession(String password, Long chatId);
    void turnOffRegistrationState(Long chatId);
    void turnOffLogInState(Long chatId);
    void handleStartingChatState(Long chatId);
    boolean checkIfUserHasSession(Long chatId);
    void handleLogoutRequest(Long chatId);
    boolean checkIfLoginRequest(Long chatId);
    String getUsernameFromUserSessionByChatId(Long chatId);
    TelegramResponse startSendConversationState(Long chatId);
    boolean checkIfEmailConversationStateOn(Long chatId);
    void turnOnEmailConversationState(Long chatId);
    void turnOffEmailConversationState(Long chatId);
}
