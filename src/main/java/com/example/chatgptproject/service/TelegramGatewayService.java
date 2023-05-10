package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramResponse;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public interface TelegramGatewayService {
    TelegramResponse handleTelegramRequest(@NotNull Update update)
            throws IOException, InterruptedException;
}
