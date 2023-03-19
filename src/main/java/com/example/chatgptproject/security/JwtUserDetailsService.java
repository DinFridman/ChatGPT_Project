package com.example.chatgptproject.security;

import com.example.chatgptproject.model.AppUser;
import com.example.chatgptproject.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(appUserRepository.existsAppUserByUsername(username))
            log.info("exists!");
        Optional<AppUser> user = appUserRepository.findAppUserByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("username Not found" + username);
        }
        return new User(user.get().getUsername(), user.get().getPassword(), new ArrayList<>());
    }
}
