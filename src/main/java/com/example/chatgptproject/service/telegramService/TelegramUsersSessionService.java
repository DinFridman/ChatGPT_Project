package com.example.chatgptproject.service.telegramService;

import com.example.chatgptproject.model.UserSessionDetails;

public interface TelegramUsersSessionService {
    void createNewSessionForUser(UserSessionDetails userSessionDetails);
    UserSessionDetails getUserSessionDetailsFromRepo(Long chatId);
    void addUserSessionDetails(UserSessionDetails userSessionDetails);
    void updateUserSessionDetails(UserSessionDetails userSessionDetails);
    void removeUserSessionDetails(Long chatId);
    boolean checkIfUserSessionExistsByChatId(Long chatId);
    UserSessionDetails createUserSessionDetailsFromChatId(Long chatId);
}
