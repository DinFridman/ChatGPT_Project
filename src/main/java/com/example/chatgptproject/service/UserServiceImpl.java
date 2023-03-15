package com.example.chatgptproject.service;

import com.example.chatgptproject.model.AppRoleEntity;
import com.example.chatgptproject.model.ApplicationUser;
import com.example.chatgptproject.repository.ApplicationUserRepository;
import com.example.chatgptproject.utils.enums.AppRoles;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = LogManager.getLogger("UserService-Logger");
    private final ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> user =
                applicationUserRepository.findApplicationUserByUsername(username);
        if(!user.isPresent()) {
            logger.error("User not found in database!");
            throw new UsernameNotFoundException("User not found in database!");
        } else {
            logger.info("User {} found in datbase", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AppRoles.USER.toString()));
        return new org.springframework.security.core.userdetails.User(
                 user.get().getUsername(),user.get().getPassword(),authorities);
    }

    @Override
    public ApplicationUser saveUser(ApplicationUser user) {//TODO: add log after operation
        logger.info("-------------saving user: {} -------------", user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);
        return applicationUserRepository.save(user);
    }

    @Override
    public void addRoleToUser(String userName, AppRoleEntity role) {
        Optional<ApplicationUser> user = getUser(userName);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(userName);
        }
        logger.info("-------------Changing user: {}  role to {}-------------",userName,role);
        applicationUserRepository.delete(user.get());
        logger.debug("-------------User has removed successfully-------------");
        Collection<AppRoleEntity> appRoleEntityList = user.get().getAppRoleEntityList();
        appRoleEntityList.add(role);
        user.get().setAppRoleEntityList(appRoleEntityList);
        saveUser(user.get());
        logger.debug("-------------Changed user`s role successfully-------------");
    }

    @Override
    public Optional<ApplicationUser> getUser(String userName) {
        logger.debug("-------------Fetching user -------------");
       return applicationUserRepository.findApplicationUserByUsername(userName);
    }


}
