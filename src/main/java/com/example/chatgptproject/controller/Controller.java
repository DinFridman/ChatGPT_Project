package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.model.EmailDetails;
import com.example.chatgptproject.service.EmailServiceImpl;
import com.example.chatgptproject.service.TelegramRequestHandler;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telegram")
public class Controller {
    private final TelegramRequestHandler telegramRequestHandler;
    private static final Logger logger = LogManager.getLogger("controller-logger");
    private final ChatMessageDTOMapper chatMessageDTOMapper;
    private final EmailServiceImpl emailService;

    @PostMapping("/generateAnswer")
    public ResponseEntity<TelegramResponseDTO> generateAnswer(@RequestBody Update request)
            throws IOException, InterruptedException {
        logger.info("---------------Request : " + request + " ---------------");

        TelegramResponseDTO response = telegramRequestHandler
                .handleTelegramRequest(chatMessageDTOMapper.mapToDTO(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

}
