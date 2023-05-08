package com.example.chatgptproject.service;

import com.example.chatgptproject.dto.TelegramKeyBoardMessageDTO;
import com.example.chatgptproject.dto.TelegramResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class TelegramKeyboardServiceImpl implements TelegramKeyboardService{

    private ReplyKeyboardMarkup createKeyboard(List<KeyboardButton> keyboardButtonList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        addButtonListToKeyBoard(keyboardRow, keyboardButtonList);

        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Override
    public TelegramResponse createTelegramResponseWithChatMainKeyboard(Long chatId,
                                                                           String text) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createMainChatKeyboard();

        TelegramResponse telegramKeyBoardMessageDTO =
                createTelegramKeyBoardResponse(chatId, text, replyKeyboardMarkup);

        log.info("telegram main Keyboard Response created successfully : {}"
                ,telegramKeyBoardMessageDTO);

        return telegramKeyBoardMessageDTO;
    }

    private ReplyKeyboardMarkup createMainChatKeyboard() {
        List<KeyboardButton> keyboardButtonList = createMainChatButtonList();

        return createKeyboard(keyboardButtonList);
    }

    private List<KeyboardButton> createMainChatButtonList() {
        List<KeyboardButton> mainChatButtonList =  new ArrayList<>();
        KeyboardButton registerButton = new KeyboardButton();
        KeyboardButton loginButton = new KeyboardButton();

        setTextToButton(registerButton,"Logout");
        setTextToButton(loginButton,"Email conversation");

        mainChatButtonList.add(registerButton);
        mainChatButtonList.add(loginButton);

        return mainChatButtonList;
    }

    private void setTextToButton(KeyboardButton keyboardButton, String text) {
        keyboardButton.setText(text);
    }

    private void addButtonListToKeyBoard(KeyboardRow keyboardRow,
                                                List<KeyboardButton> keyboardButtonList) {
        keyboardRow.addAll(keyboardButtonList);
    }

    @Override
    public TelegramResponse createTelegramResponseWithLoginRegisterKeyboard(
            Long chatId, String text) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createLoginRegisterKeyboard();

        TelegramResponse telegramKeyBoardMessageDTO =
                createTelegramKeyBoardResponse(chatId, text, replyKeyboardMarkup);

        log.info("telegramKeyboardResponse created successfully : {}",telegramKeyBoardMessageDTO);

        return telegramKeyBoardMessageDTO;
    }

    private ReplyKeyboardMarkup createLoginRegisterKeyboard() {
        List<KeyboardButton> keyboardButtonList = createLoginAndRegisterButtonList();

        return createKeyboard(keyboardButtonList);
    }

    private List<KeyboardButton> createLoginAndRegisterButtonList() {
        List<KeyboardButton> registerAndLoginButtonList =  new ArrayList<>();
        KeyboardButton registerButton = new KeyboardButton();
        KeyboardButton loginButton = new KeyboardButton();

        setTextToButton(registerButton,"Register");
        setTextToButton(loginButton,"Login");

        registerAndLoginButtonList.add(registerButton);
        registerAndLoginButtonList.add(loginButton);

        return registerAndLoginButtonList;
    }

    private TelegramResponse createTelegramKeyBoardResponse(
            Long chatId, String text, ReplyKeyboardMarkup keyboard) {

        return TelegramKeyBoardMessageDTO.builder()
                .chatId(chatId)
                .text(text)
                .keyboard(keyboard)
                .build();
    }
}
