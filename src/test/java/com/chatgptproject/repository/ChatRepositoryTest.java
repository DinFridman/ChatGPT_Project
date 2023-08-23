package com.chatgptproject.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ChatRepositoryTest {
   /* @Autowired
    private ChatRepository underTest;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Container
    static PostgreSQLContainer postgresqlContainer =
            new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("Din5675671");

    @BeforeEach
    void initUseCase() {
        List<ChatMessageEntity> messages = Arrays.asList(
                new ChatMessageEntity(1,1,"default", Roles.USER.toString())
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
                Roles.USER.toString()
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
                new ChatMessageEntity(2000,chatId,"Hello", Roles.USER.toString()),
                new ChatMessageEntity(2001,chatId,"Hi", Roles.USER.toString()),
                new ChatMessageEntity(2002,3,"Tell me something",
                        Roles.USER.toString())
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
                new ChatMessageEntity(2000,chatId,"Hello", Roles.USER.toString()),
                new ChatMessageEntity(2001,chatId,"Hi", Roles.USER.toString()),
                new ChatMessageEntity(2002,3,"Tell me something",
                        Roles.USER.toString())
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
                new ChatMessageEntity(1000,1,"Hello", Roles.USER.toString()),
                new ChatMessageEntity(1001,1,"Hi", Roles.USER.toString()),
                new ChatMessageEntity(1002,2,"Tell me something",
                        Roles.USER.toString())
        );
        Iterable<ChatMessageEntity> allMessages = underTest.saveAll(messages);

        //when
        AtomicInteger validIdFound = new AtomicInteger();
        allMessages.forEach(message -> {
            if(message.getUser().getUserId()>0){
                validIdFound.getAndIncrement();
            }
        });

        //then
        assertThat(validIdFound.intValue()).isEqualTo(3);
    }*/
}