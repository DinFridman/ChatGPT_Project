package com.chatgptproject.dto.openAI;

record UsageDTO(Integer prompt_tokens, Integer completion_tokens, Integer total_tokens){
}
