package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.ChatMessageEntity;
import com.example.chatgptproject.utils.enums.ChatRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ChatRepositoryTest {

    @Autowired
    private ChatRepository underTest;

    @BeforeEach
    void initUseCase() {
        List<ChatMessageEntity> messages = Arrays.asList(
                new ChatMessageEntity(1,1,"default", 1, ChatRoles.USER.toString())
        );
        underTest.saveAll(messages);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfMessageExistsByChatId() {
        //given
        long chatId = 1;
        ChatMessageEntity message = new ChatMessageEntity(
                1000,
                chatId,
                "Hi",
                1001,
                ChatRoles.USER.toString()
        );
        underTest.save(message);

        //when
        boolean expected = underTest.existsChatMessageEntityByChatId(chatId);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfMessageNotExistsByChatId() {
        //given
        long chatId = 12345;

        //when
        boolean expected = underTest.existsChatMessageEntityByChatId(chatId);

        //then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldNotReturnAnyMessageByChatId() {
        //given
        long chatId = 2;
        long notExistsChatId = 10100;
        List<ChatMessageEntity> entities = Arrays.asList(
                new ChatMessageEntity(2000,chatId,"Hello",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(2001,chatId,"Hi",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(2002,3,"Tell me something",10001,
                        ChatRoles.USER.toString())
        );
        underTest.saveAll(entities);

        //when
        ArrayList<ChatMessageEntity> messages =
                underTest.findMessagesByChatId(notExistsChatId);

        //then
        assertThat(messages.size()).isEqualTo(0);
    }

    @Test
    void itShouldReturnMessagesByChatId() {
        //given
        long chatId = 2;
        List<ChatMessageEntity> entities = Arrays.asList(
                new ChatMessageEntity(2000,chatId,"Hello",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(2001,chatId,"Hi",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(2002,3,"Tell me something",10001,
                        ChatRoles.USER.toString())
        );
        underTest.saveAll(entities);

        //when
        ArrayList<ChatMessageEntity> messages =
                underTest.findMessagesByChatId(chatId);

        //then
        assertThat(messages.size()).isEqualTo(2);
    }

    @Test
    void shouldSaveAll() {
        //given
        List<ChatMessageEntity> messages = Arrays.asList(
                new ChatMessageEntity(1000,1,"Hello",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(1001,1,"Hi",10000, ChatRoles.USER.toString()),
                new ChatMessageEntity(1002,2,"Tell me something",10001,
                        ChatRoles.USER.toString())
        );
        Iterable<ChatMessageEntity> allMessages = underTest.saveAll(messages);

        //when
        AtomicInteger validIdFound = new AtomicInteger();
        allMessages.forEach(message -> {
            if(message.getUserId()>0){
                validIdFound.getAndIncrement();
            }
        });

        //then
        assertThat(validIdFound.intValue()).isEqualTo(3);
    }
}