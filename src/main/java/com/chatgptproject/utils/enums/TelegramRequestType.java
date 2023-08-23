package com.chatgptproject.utils.enums;

public enum TelegramRequestType {
    EMPTY_OR_NULL_MESSAGE("emptyOrNull"),
    NO_SESSION("noSession"),
    LOGIN_BUTTON_PRESSED("loginButtonPressed"),
    REGISTER_BUTTON_PRESSED("registerButtonPressed"),
    LOGOUT_BUTTON_PRESSED("logoutButtonPressed"),
    REGISTER_REQUEST("registerRequest"),
    LOGIN_REQUEST("loginRequest"),
    NOT_LOGGED_IN("notLoggedIn"),
    SEND_CONVERSATION_BUTTON_PRESSED("sendConversationButtonPressed"),
    SEND_CONVERSATION_REQUEST("sendConversationRequest"),
    GENERATE_ANSWER("generateAnswer");
    private final String requestType;

    TelegramRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return requestType;
    }
}
