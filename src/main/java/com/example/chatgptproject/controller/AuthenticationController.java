package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.LoginUserDTO;
import com.example.chatgptproject.security.payload.request.AuthRequest;
import com.example.chatgptproject.security.dto.RegisterDTO;
import com.example.chatgptproject.security.payload.request.RegisterRequest;
import com.example.chatgptproject.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authService.registerUser(modelMapper.map(registerRequest,RegisterDTO.class));
        return new ResponseEntity<>("user saved successfully!", HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest authRequest) {
        ResponseCookie cookie = authService.loginUser(
                modelMapper.map(authRequest, LoginUserDTO.class));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("user logged in successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = authService.logoutUser();
        log.info("user logged out successfully.");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("user logged out successfully.");
    }
}
