package com.chatgptproject.service.telegramService;

import com.chatgptproject.dto.TelegramResponse;
import com.chatgptproject.model.UserSessionDetails;

public interface TelegramUserStateService {
    TelegramResponse startRegisterState(Long chatId);
    boolean isRegisterRequest(Long chatId);
    boolean isUserLoggedIn(Long chatId);
    UserSessionDetails getUserSessionDetails(Long chatId);
    TelegramResponse startLoginState(Long chatId);
    boolean isUsernameProvided(Long chatId);
    void setUsernameToUserSession(String username, UserSessionDetails userSessionDetails);
    void setPasswordToUserSession(String password, UserSessionDetails userSessionDetails);
    void turnOffRegistrationState(UserSessionDetails userSessionDetails);
    void turnOnLoggedInState(UserSessionDetails userSessionDetails);
    void handleStartingChatState(Long chatId);
    boolean checkIfUserHasSession(Long chatId);
    void handleLogoutRequest(Long chatId);
    boolean checkIfLoginRequest(Long chatId);
    String getUsernameFromUserSessionByChatId(Long chatId);
    TelegramResponse startSendConversationState(Long chatId);
    boolean checkIfEmailConversationStateOn(Long chatId);
    void turnOnEmailConversationState(Long chatId);
    void turnOffEmailConversationState(Long chatId);
    void handleSuccessfulLogin(UserSessionDetails userSessionDetails);
}
