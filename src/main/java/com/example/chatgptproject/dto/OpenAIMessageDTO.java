package com.example.chatgptproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenAIMessageDTO {
    private final String role;
    private final String content;
}
