package com.example.chatgptproject.utils;

import com.example.chatgptproject.service.TelegramUserStateServiceImpl;
import com.example.chatgptproject.utils.enums.TelegramRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramRequestTypeResolver {
    private final TelegramUserStateServiceImpl telegramUserStateServiceImpl;
    private TelegramRequestType telegramRequestType;

    public TelegramRequestType resolve(String message, long chatId) {
        if (messageIsEmptyOrNull(message))
            createTelegramRequestType(TelegramRequestType.EMPTY_OR_NULL_MESSAGE);

        else if (checkIfUserDoesntHasSession(chatId))
            createTelegramRequestType(TelegramRequestType.NO_SESSION);

        else if (isLoginButtonPressed(message))
            createTelegramRequestType(TelegramRequestType.LOGIN_BUTTON_PRESSED);

        else if (isRegisterButtonPressed(message))
            createTelegramRequestType(TelegramRequestType.REGISTER_BUTTON_PRESSED);

        else if (isLogoutButtonPressed(message))
            createTelegramRequestType(TelegramRequestType.LOGOUT_BUTTON_PRESSED);

        else if (isSendConversationButtonPressed(message))
            createTelegramRequestType(TelegramRequestType.SEND_CONVERSATION_BUTTON_PRESSED);

        else if (isLoginRequest(chatId))
            createTelegramRequestType(TelegramRequestType.LOGIN_REQUEST);

        else if (isRegisterRequest(chatId))
            createTelegramRequestType(TelegramRequestType.REGISTER_REQUEST);

        else if (isSendConversationRequest(chatId))
            createTelegramRequestType(TelegramRequestType.SEND_CONVERSATION_REQUEST);

        else if (userIsNotLoggedIn(chatId))
            createTelegramRequestType(TelegramRequestType.NOT_LOGGED_IN);

        else
            createTelegramRequestType(TelegramRequestType.GENERATE_ANSWER);

        return telegramRequestType;
    }

    private void createTelegramRequestType(TelegramRequestType requestedType) {
        telegramRequestType = requestedType;
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
