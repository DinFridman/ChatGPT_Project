package com.example.chatgptproject.dto;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class ConversationDTO {
    private ArrayList<OpenAIPromptDTO> conversation;
}
