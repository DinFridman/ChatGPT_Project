package com.example.chatgptproject.controller;

import com.example.chatgptproject.bot.TelegramBot;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.service.TelegramGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramController {
    private final TelegramGatewayService telegramGatewayService;
    private final TelegramBot telegramBot;
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    /*@BotMapping(value = "/login/createSession")
    public void createLoginSession(@MessageParam Message message)
            throws IOException, InterruptedException {

        log.info("---------------username Message : " + message + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.createUserLoginSession(message);
        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }

    @BotMapping(value = "/login/performLogin")
    public void performLogin(@MessageParam Message message)
            throws IOException, InterruptedException {
        log.info("---------------Password Message : " + message + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.performUserLogin(message);
        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }

    @BotMapping(value = "/register/createSession")
    public void createRegistrationSession(@MessageParam Message message)
            throws IOException, InterruptedException {

        log.info("---------------username Message : " + message + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.createUserRegistrationSession(message);
        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }

    @BotMapping(value = "/register/performRegistration")
    public void performRegistration(@MessageParam Message message)
            throws IOException, InterruptedException {

        log.info("---------------Password Message : " + message + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.performUserRegistration(message);
        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }

    @BotMapping(value = "/logout")
    public void performLogout(@ChatParam Chat chat)
            throws IOException, InterruptedException {

        log.info("---------------user chat details : " + chat + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.performLogout(chat.getId());
        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }

    @BotMapping
    public void generateAnswer(@UpdateParam Update update)
            throws IOException, InterruptedException {

        log.info("---------------chat completion`s update details : " + update + " ---------------");

        TelegramResponseDTO telegramResponseDTO =
                telegramGatewayService.handleGenerateAnswerState(
                        chatMessageDTOMapper.mapToDTO(update));

        telegramBot.sendTelegramMessage(telegramResponseDTO);
    }*/

}
