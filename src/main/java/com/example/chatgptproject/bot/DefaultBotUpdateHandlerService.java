package com.example.chatgptproject.bot;

import com.orgyflame.springtelegrambotapi.api.object.Update;
import com.orgyflame.springtelegrambotapi.api.service.TelegramApiService;
import com.orgyflame.springtelegrambotapi.bot.BotUpdateHandlerService;
import com.orgyflame.springtelegrambotapi.bot.container.BotApiMappingContainer;
import com.orgyflame.springtelegrambotapi.bot.mapping.BotApiMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.orgyflame.springtelegrambotapi.bot.BotUtil.parseUpdateForParameter;

@Service
@RequiredArgsConstructor
public class DefaultBotUpdateHandlerService implements BotUpdateHandlerService {
    private final BotApiMappingContainer botApiMappingContainer;

    @Override
    public void handle(Update update) {
        handleMessage(update);
    }

    private void handleMessage(Update update) {
        String path;
        BotApiMapping mapping = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            path = update.getMessage().getText().split(" ")[0].trim();
            if (path.charAt(0) == '/') {
                mapping = botApiMappingContainer.getBotApiMapping(path);

                update.getMessage().setText(
                        Arrays.stream(update.getMessage().getText().split(" "))
                                .skip(1)
                                .reduce((s, s2) -> s + s2)
                                .orElse("")
                                .trim()
                );
            }

        }

        if (mapping == null) mapping = botApiMappingContainer.getBotApiMapping("");
        List<Object> args = parseParameters(mapping.getMethod().getParameters(), update);

        try {
            mapping.execute(args.toArray());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private List<Object> parseParameters(Parameter[] parameters, Update update) {
        List<Object> res = new ArrayList<>();

        for (Parameter parameter : parameters) {
            Object obj = parseUpdateForParameter(parameter, update);
            res.add(obj);
        }
        return res;
    }
}
