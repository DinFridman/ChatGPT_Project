package com.chatgptproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@SpringBootApplication
public class ChatGptProjectApplication {

    public static void main(String[] args){SpringApplication.run(ChatGptProjectApplication.class, args);}

}
