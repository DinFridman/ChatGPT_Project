    package com.example.chatgptproject.utils.enums;


public enum OpenAiModels {
    CHAT_COMPLETION("gpt-3.5-turbo"),
    TEST_COMPLETION("text-davinci-003");
    private String modelName;

    OpenAiModels(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {return modelName; }
}
