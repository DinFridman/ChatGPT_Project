package com.example.chatgptproject.dto.openAI;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
public class OpenAIResponseDTO {
    String id;
    String object;
    Date created;
    String model;
    List<ChoiceDTO> choices;
    UsageDTO usage;
}
