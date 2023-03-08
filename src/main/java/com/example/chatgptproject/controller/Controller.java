package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.ChatAnswerDTO;
import com.example.chatgptproject.service.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.api.objects.Update;

@RestController
public class Controller {
    private final RequestService requestService;
    private static final Logger logger = LogManager.getLogger("Controller-logger");

    public Controller(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/")
    public ChatAnswerDTO generateAnswer(@RequestBody Update request) throws JsonProcessingException, JSONException {
        logger.info("---------------Request : " + request + " ---------------");
        return requestService.generateAnswer(request);
    }
}
