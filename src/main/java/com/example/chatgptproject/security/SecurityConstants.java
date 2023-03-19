package com.example.chatgptproject.security;

import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 70000;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
}
