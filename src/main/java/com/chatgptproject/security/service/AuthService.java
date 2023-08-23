package com.chatgptproject.security.service;

import com.chatgptproject.model.AppUserEntity;
import com.chatgptproject.dto.LoginUserDTO;
import com.chatgptproject.security.dto.RegisterDTO;
import com.chatgptproject.security.jwt.JWTUtils;
import com.chatgptproject.service.AppUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final AppUserServiceImpl appUserService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void loginUser(LoginUserDTO loginUserDTO) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginUserDTO.getUsername(),loginUserDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User signed in successfully!");

        appUserService.updateAppUserLoggedInDate(loginUserDTO.getUsername());
    }

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        AppUserEntity user = createAppUser(registerDTO);

        appUserService.addAppUser(user);

        log.info("user saved successfully!");
    }

    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    public AppUserEntity createAppUser(RegisterDTO registerDTO) {
        AppUserEntity user = new AppUserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return user;
    }

    public Boolean checkIfAppUserExists(String username) {
        return appUserService.checkIfAppUserExistsByUsername(username);
    }
}
