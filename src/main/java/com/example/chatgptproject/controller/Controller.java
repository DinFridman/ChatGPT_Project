package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.dto.mapper.ChatMessageDTOMapper;
import com.example.chatgptproject.service.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.api.objects.Update;

@RestController
public class Controller {
    private final RequestService requestService;
    private static final Logger logger = LogManager.getLogger("controller-logger");
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    public Controller(RequestService requestService,
                      ChatMessageDTOMapper chatMessageDTOMapper) {
        this.requestService = requestService;
        this.chatMessageDTOMapper = chatMessageDTOMapper;
    }

    @RequestMapping("/")
    public ResponseEntity<ChatAnswerDTO> generateAnswer(@RequestBody Update request) throws JsonProcessingException, JSONException {
        logger.info("---------------Request : " + request + " ---------------");

        ChatAnswerDTO response = requestService
                .generateAnswer(chatMessageDTOMapper.mapToDTO(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
