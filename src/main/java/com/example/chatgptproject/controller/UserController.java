package com.example.chatgptproject.controller;

import com.example.chatgptproject.model.ApplicationUser;
import com.example.chatgptproject.model.UserEntity;
import com.example.chatgptproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        return ResponseEntity.ok().body("no users yet");
    }

    @PostMapping("/users/save")
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUser user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }
}
