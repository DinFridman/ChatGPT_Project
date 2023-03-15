package com.example.chatgptproject.service;

import com.example.chatgptproject.model.ApplicationUser;
import com.example.chatgptproject.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> applicationUser =
                applicationUserRepository.findApplicationUserByUsername(username);
        if (!applicationUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.get().getUsername(),
                applicationUser.get().getPassword(),
                emptyList());
    }
}
