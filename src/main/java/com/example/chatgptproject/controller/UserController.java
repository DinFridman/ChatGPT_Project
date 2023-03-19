package com.example.chatgptproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @RequestMapping({ "/hello" })
    public String firstPage() {
        return "Hello World";
    }
}
