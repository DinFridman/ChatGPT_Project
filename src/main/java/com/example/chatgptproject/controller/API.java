package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.TelegramMessageResponseDTO;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.service.TelegramRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telegram")
public class API {
    private final TelegramRequestServiceImpl telegramRequestServiceImpl;
    private static final Logger logger = LogManager.getLogger("controller-logger");
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    /*@PostMapping("/generateAnswer")
    public ResponseEntity<TelegramMessageResponseDTO> generateAnswer(@RequestBody Update request)
            throws IOException, InterruptedException {
        logger.info("---------------Request : " + request + " ---------------");

        TelegramMessageResponseDTO response = telegramRequestServiceImpl
                .handleTelegramRequest(chatMessageDTOMapper.mapToDTO(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/

}
