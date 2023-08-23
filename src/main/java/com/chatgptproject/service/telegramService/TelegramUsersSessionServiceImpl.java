package com.chatgptproject.service.telegramService;

import com.chatgptproject.model.UserSessionDetails;
import com.chatgptproject.repository.UserSessionDetailsRepository;
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
    public void createNewSessionForUser(UserSessionDetails userSessionDetails) {
        addUserSessionDetails(userSessionDetails);
        log.info("-----------------------" +
                "New session created for user with chatId : {}" +
                "-----------------------", userSessionDetails.getChatId());
    }

    @Override
    public UserSessionDetails createUserSessionDetailsFromChatId(Long chatId) {
        return createNewUserSessionWithChatId(chatId);
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

    @Override
    public boolean checkIfUserSessionExistsByChatId(Long chatId) {
        return userSessionDetailsRepository.existsById(chatId);
    }

    private UserSessionDetails createNewUserSessionWithChatId(Long chatId) {
       return UserSessionDetails.builder()
                .chatId(chatId)
                .build();
    }
}
