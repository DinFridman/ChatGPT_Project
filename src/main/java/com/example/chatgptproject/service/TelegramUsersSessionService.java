package com.example.chatgptproject.service;


import com.example.chatgptproject.dto.LoginUserDTO;
import com.example.chatgptproject.dto.mapper.LoginUserDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "loginUsers")
@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramUsersSessionService {
    private final LoginUserDTOMapper loginUserDTOMapper;

    @Cacheable(key = "#chatId")
    public void getLoginUser(Long chatId) {
        log.info("user : {} requested from the loginUsers cache.",chatId);
    }

    public void createSessionForUser(Long chatId) {
        LoginUserDTO loginUserDTO = createLoginUserWithChatId(chatId);
        addLoginUser(loginUserDTO);
    }

    @Cacheable(key = "#loginUserDTO.chatId")
    public LoginUserDTO addLoginUser(LoginUserDTO loginUserDTO) {
        log.info("user with chatId : {} added in the loginUsers cache."
                , loginUserDTO.getChatId());
        return loginUserDTO;
    }

    @CachePut(key = "#loginUserDTO.username")
    public LoginUserDTO updateLoginUser(LoginUserDTO loginUserDTO) {
        log.info("user : {} updated in the loginUsers cache.",loginUserDTO.getUsername());
        return loginUserDTO;
    }

    @CacheEvict(key = "#chatId")
    public void removeLoginUser(Long chatId) {
        log.info("loginUser with the chatId session is : {}" +
                " removed from the loginUsers cache.",chatId);
    }

    private LoginUserDTO createLoginUserWithChatId(Long chatId) {
        return loginUserDTOMapper.mapToDTO(chatId);
    }
}
