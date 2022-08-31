package com.example.brunch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application.properties")
class BrunchApplicationTests {

    @Value("${example.property}")
    private String exampleProperty;

    @BeforeEach
    public void init() {
    }

    @Test
    void contextLoads() {
    }

}
