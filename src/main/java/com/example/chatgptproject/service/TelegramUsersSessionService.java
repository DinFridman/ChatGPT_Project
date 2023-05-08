package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.UserSessionDetails;

public interface TelegramUsersSessionService {
    void createNewSessionForUser(Long chatId);
    UserSessionDetails getUserSessionDetailsFromRepo(Long chatId);
    void addUserSessionDetails(UserSessionDetails userSessionDetails);
    void updateUserSessionDetails(UserSessionDetails userSessionDetails);
    void removeUserSessionDetails(Long chatId);
    boolean checkIfUserSessionExistByChatId(Long chatId);
}
