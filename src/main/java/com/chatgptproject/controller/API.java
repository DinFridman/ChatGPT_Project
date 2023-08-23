package com.chatgptproject.controller;

import com.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.chatgptproject.service.telegramService.TelegramRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

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
