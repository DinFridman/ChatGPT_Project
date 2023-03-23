package com.example.chatgptproject.security.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Setter
@RequiredArgsConstructor
@Getter
public class AuthRequest {
    private String username;
    private String password;
}
