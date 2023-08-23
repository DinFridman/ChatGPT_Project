package com.chatgptproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorBody{
    private final String error;
    private final int status;
    private final String message;

}
