package com.example.chatgptproject.utils.enums;

public enum AppRoles {
    ADMIN("admin"),
    USER("user");
    private String role;

    AppRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
