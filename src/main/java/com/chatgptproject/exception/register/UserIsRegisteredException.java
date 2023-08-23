package com.chatgptproject.exception.register;

import org.springframework.http.HttpStatus;

public class UserIsRegisteredException extends RegisterAbstractException {

    public UserIsRegisteredException() {
        super("user is already registered!", HttpStatus.BAD_REQUEST);
    }
}
