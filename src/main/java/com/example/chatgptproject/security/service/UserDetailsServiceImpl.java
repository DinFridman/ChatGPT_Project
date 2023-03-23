package com.example.chatgptproject.security.service;

import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service("JwtUserDetailsService")
@Log4j2
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(appUserRepository.existsAppUserByUsername(username))
            log.info("user exists!");
        Optional<AppUser> user = appUserRepository.findAppUserByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("username Not found" + username);
        }
        return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
    }
}
