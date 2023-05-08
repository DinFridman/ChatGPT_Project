package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.UserSessionDetails;
import com.example.chatgptproject.repository.UserSessionDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramUsersSessionServiceImpl implements TelegramUsersSessionService{
    private final UserSessionDetailsRepository userSessionDetailsRepository;


    @Override
    public void createNewSessionForUser(Long chatId) {
        UserSessionDetails userSessionDetails = createNewUserSessionWithChatId(chatId);
        log.info("-----------------------" +
                "New session created for user : {}" +
                "-----------------------", userSessionDetails.getUsername());

        addUserSessionDetails(userSessionDetails);
    }

    @Override
    public UserSessionDetails getUserSessionDetailsFromRepo(Long chatId) {
        return userSessionDetailsRepository.findById(chatId).orElseThrow(
                () -> new UsernameNotFoundException("user is not found!"));
    }

    @Override
    public void addUserSessionDetails(UserSessionDetails userSessionDetails) {
        userSessionDetailsRepository.save(userSessionDetails);

        log.info("session with chatId : {} was added successfully."
                , userSessionDetails.getChatId());
    }

    @Override
    public void updateUserSessionDetails(UserSessionDetails userSessionDetails) {
        userSessionDetailsRepository.save(userSessionDetails);

        log.info("session with chatId : {} updated successfully.",
                userSessionDetails.getChatId());
    }

    @Override
    public void removeUserSessionDetails(Long chatId) {
        userSessionDetailsRepository.deleteById(chatId);

        log.info("session with the chatId : {}" +
                " has been removed.", chatId);
    }

    private UserSessionDetails createNewUserSessionWithChatId(Long chatId) {
        UserSessionDetails userSessionDetails = new UserSessionDetails();
        userSessionDetails.setChatId(chatId);
        return userSessionDetails;
    }

    @Override
    public boolean checkIfUserSessionExistByChatId(Long chatId) {
        return userSessionDetailsRepository.existsById(chatId);
    }
}
