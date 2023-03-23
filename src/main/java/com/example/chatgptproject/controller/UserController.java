package com.example.chatgptproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @PostMapping({ "/test" })
    public String firstPage() {
        return "Hello World";
    }
}
