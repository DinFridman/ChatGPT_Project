package com.example.chatgptproject.utils;

import com.example.chatgptproject.service.TelegramUserStateServiceImpl;
import com.example.chatgptproject.utils.enums.TelegramRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramRequestTypeResolver {
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;

    public TelegramRequestType resolve(String message, long chatId) {
        if (messageIsEmptyOrNull(message)) {
            return TelegramRequestType.EMPTY_OR_NULL_MESSAGE;
        }
        if (checkIfUserDoesntHasSession(chatId))
            return TelegramRequestType.NO_SESSION;

        if (isLoginButtonPressed(message))
            return TelegramRequestType.LOGIN_BUTTON_PRESSED;

        if (isRegisterButtonPressed(message))
            return TelegramRequestType.REGISTER_BUTTON_PRESSED;

        if (isLogoutButtonPressed(message))
            return TelegramRequestType.LOGOUT_BUTTON_PRESSED;

        if (isSendConversationButtonPressed(message))
            return TelegramRequestType.SEND_CONVERSATION_BUTTON_PRESSED;

        if (isLoginRequest(chatId))
            return TelegramRequestType.LOGIN_REQUEST;

        if (isRegisterRequest(chatId))
            return TelegramRequestType.REGISTER_REQUEST;

        if (isSendConversationRequest(chatId))
           return TelegramRequestType.SEND_CONVERSATION_REQUEST;

        if (userIsNotLoggedIn(chatId))
            return TelegramRequestType.NOT_LOGGED_IN;

        return TelegramRequestType.GENERATE_ANSWER;
    }

    private boolean messageIsEmptyOrNull(String message) {
        return (message == null || message.isEmpty());
    }

    private boolean checkIfUserDoesntHasSession(Long chatId) {
        return !telegramUserStateServiceImpl.checkIfUserHasSession(chatId);
    }

    private boolean isLoginButtonPressed(String message) {
        return message.equals("Login");
    }

    private boolean isRegisterButtonPressed(String message) {
        return message.equals("Register");
    }

    private boolean isLogoutButtonPressed(String message) {
        return message.equals("Logout");
    }

    private boolean isSendConversationButtonPressed(String message) {
        return message.equals("Email conversation");
    }

    private boolean isLoginRequest(Long chatId) {
        return telegramUserStateServiceImpl.checkIfLoginRequest(chatId);
    }

    private boolean isRegisterRequest(Long chatId) {
        return telegramUserStateServiceImpl.checkIfRegisterRequest(chatId);
    }

    private boolean isSendConversationRequest(Long chatId) {
        return telegramUserStateServiceImpl.checkIfEmailConversationStateOn(chatId);
    }

    private boolean userIsNotLoggedIn(Long chatId) {
        return !telegramUserStateServiceImpl.checkIfUserLoggedIn(chatId);
    }
}
