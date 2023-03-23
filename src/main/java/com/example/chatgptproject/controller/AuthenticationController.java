package com.example.chatgptproject.controller;

import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.repository.AppUserRepository;
import com.example.chatgptproject.security.payload.response.AuthResponse;
import com.example.chatgptproject.security.dto.LoginDTO;
import com.example.chatgptproject.security.dto.RegisterDTO;
import com.example.chatgptproject.security.jwt.JWTUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/users")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if(appUserRepository.existsAppUserByUsername(registerDTO.getUsername()))
            return new ResponseEntity<>("username is taken!", HttpStatus.BAD_REQUEST);

        AppUser user = new AppUser();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        appUserRepository.save(user);

        log.info("user saved successfully!");

        return new ResponseEntity<>("user saved successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User signed in successfully!");

        String token = jwtUtils.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }
}
