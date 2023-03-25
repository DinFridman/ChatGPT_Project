package com.example.chatgptproject.security.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    @JsonAlias("phone_number")
    private String phoneNumber;
}
