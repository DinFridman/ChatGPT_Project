package com.example.chatgptproject.utils;

public class Constants {
    public static final String OPEN_AI_KEY = "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";
    public static final String CHAT_COMPLETION_URL = "https://api.openai.com/v1/chat/completions";
    public static final String TELEGRAM_BOT_NAME = "chatGPT_project_bot";
    public static final String TELEGRAM_URL = "https://api.telegram.org/bot";
    public static final String SEND_MESSAGE_URL = "/sendMessage";
    public static final String TELEGRAM_BOT_TOKEN = "6246049032:AAGmOnVsazp_v749C9Mmu6v3uwpq89W1QW8";
    public static final String TEXT_COMPLETION_URL = "https://api.openai.com/v1/completions";
    public static final String USER_ROLE = "user";
    public static final Integer OPEN_AI_MAX_TOKENS = 2000;
    public static final double OPEN_AI_TEMPERATURE = 0.8;
    public static final String EMAIL_SUBJECT = "Your conversation with ChatGPT";
    public static final String SHARE_CONVERSATION_BY_EMAIL_REQUEST = "/sendConversation";
    public static final String SHARE_CONVERSATION_BY_EMAIL_RESPONSE =
            "Sure! Your conversation is waiting for you in your inbox.";
}
