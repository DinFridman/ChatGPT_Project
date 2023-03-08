package com.example.chatgptproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ChatGptProjectApplicationTests {

    Calculator under_test = new Calculator();

    @Test
    void itShouldAddNumbers() {
        int a = 20;
        int b = 30;

        int result = under_test.add(a,b);

        assertThat(result).isEqualTo(50);
    }
    class Calculator {
        int add(int a,int b) {
            return a+b;
        }
    }

}
