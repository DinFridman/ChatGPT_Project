package com.example.chatgptproject.security.service;

import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.repository.AppUserRepository;
import com.example.chatgptproject.security.dto.LoginUserDTO;
import com.example.chatgptproject.security.dto.RegisterDTO;
import com.example.chatgptproject.security.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @Transactional
    public ResponseCookie loginUser(LoginUserDTO loginUserDTO) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginUserDTO.getUsername(),loginUserDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User signed in successfully!");

        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        return jwtCookie;
    }

    @Transactional
    public Boolean registerUser(RegisterDTO registerDTO) {
        if(appUserRepository.existsAppUserByUsername(registerDTO.getUsername()))
            return false;

        AppUser user = new AppUser();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        appUserRepository.save(user);

        log.info("user saved successfully!");

        return true;
    }
}
