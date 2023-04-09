package com.example.chatgptproject.controller;

import com.example.chatgptproject.dto.TelegramResponseDTO;
import com.example.chatgptproject.security.dto.LoginUserDTOMapper;
import com.example.chatgptproject.security.service.AuthService;
import com.example.chatgptproject.service.TelegramGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api")
public class TelegramController {
    private final TelegramGatewayService telegramGatewayService;
    private final AuthService authService;
    private final LoginUserDTOMapper loginUserDTOMapper;

   /* @PostMapping("/gateway")
    public ResponseEntity<TelegramResponseDTO> generateAnswer(@Valid @RequestBody Update request)
            throws IOException, InterruptedException {
        log.info("---------------Request : " + request + " ---------------");

        return telegramGatewayService.telegramRequestsGateway(request);
    }*/
}
