package com.chatgptproject.dto.openAI;

import lombok.Data;

@Data
public class ChoiceDTO {
    private OpenAIPromptDTO message;
    private Integer index;
    private Integer logprobs;
    private String finish_reason;
}
