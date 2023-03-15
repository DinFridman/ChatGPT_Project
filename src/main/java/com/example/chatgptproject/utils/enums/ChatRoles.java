package com.example.chatgptproject.utils.enums;

public enum ChatRoles {
    SYSTEM("system"),
    USER("user"),
    ASSISTANCE("assistant");
    private String role;

    ChatRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
