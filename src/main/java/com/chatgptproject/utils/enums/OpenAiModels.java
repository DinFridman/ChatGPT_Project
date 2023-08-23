    package com.chatgptproject.utils.enums;


public enum OpenAiModels {
    CHAT_COMPLETION_GPT_3("gpt-3.5-turbo"),
    CHAT_COMPLETION_GPT_4("gpt-4"),
    TEST_COMPLETION("text-davinci-003");
    private final String modelName;

    OpenAiModels(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {return modelName; }
}
