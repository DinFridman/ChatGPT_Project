package com.example.chatgptproject.model;

import com.example.chatgptproject.dto.OpenAIMessageDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class Conversation {
    private final ArrayList<OpenAIMessageDTO> conversation;


}
