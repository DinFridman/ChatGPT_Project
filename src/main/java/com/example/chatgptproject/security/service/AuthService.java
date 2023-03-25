package com.example.chatgptproject.security.service;

import com.example.chatgptproject.exception.register.UserIsRegisteredException;
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
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

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
    public void registerUser(RegisterDTO registerDTO) {
        if(appUserRepository.existsAppUserByUsername(registerDTO.getUsername()))
            throw new UserIsRegisteredException();

        AppUser user = createAppUser(registerDTO);
        appUserRepository.save(user);

        log.info("user saved successfully!");
    }

    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    public AppUser createAppUser(RegisterDTO registerDTO) {
        AppUser user = new AppUser();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        return user;
    }
}
