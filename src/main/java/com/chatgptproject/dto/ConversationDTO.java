package com.chatgptproject.dto;


import com.chatgptproject.dto.openAI.OpenAIPromptDTO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Builder
public class ConversationDTO implements Serializable {
    private ArrayList<OpenAIPromptDTO> conversation;
}
