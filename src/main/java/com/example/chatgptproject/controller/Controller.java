package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.service.ChatCompletionRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.api.objects.Update;

@RestController
@RequestMapping("/api/telegram")
public class Controller {
    private final ChatCompletionRequestService chatCompletionRequestService;
    private static final Logger logger = LogManager.getLogger("controller-logger");
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    public Controller(ChatCompletionRequestService chatCompletionRequestService,
                      ChatMessageDTOMapper chatMessageDTOMapper) {
        this.chatCompletionRequestService = chatCompletionRequestService;
        this.chatMessageDTOMapper = chatMessageDTOMapper;
    }

    @PostMapping("/generateAnswer")
    public ResponseEntity<TelegramResponseDTO> generateAnswer(@RequestBody Update request) throws Exception {
        logger.info("---------------Request : " + request + " ---------------");

        TelegramResponseDTO response = chatCompletionRequestService
                .handleTelegramRequest(chatMessageDTOMapper.mapToDTO(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
