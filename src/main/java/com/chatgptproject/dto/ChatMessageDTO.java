package com.chatgptproject.dto;

import lombok.*;

@Builder
@Data
public class ChatMessageDTO {
    private final Long updateId;
    private final Long chatId;
    private final String message;
    private final String username;
    private final String role;

}
