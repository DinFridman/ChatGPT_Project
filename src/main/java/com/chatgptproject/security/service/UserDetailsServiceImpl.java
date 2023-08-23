package com.chatgptproject.security.service;

import com.chatgptproject.model.AppUserEntity;
import com.chatgptproject.repository.AppUserRepository;
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
        if(appUserRepository.existsAppUserEntityByUsername(username))
            log.info("user {} loaded successfully.", username);
        Optional<AppUserEntity> user = appUserRepository.findAppUserEntityByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("username Not found" + username);
        }
        return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
    }
}
