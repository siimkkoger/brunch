package com.example.brunch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application.properties")
class BrunchApplicationTests {

    String username = "user1";
    String password = "test";
    String jwt_expired = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY2MjA0MTQ1NH0.X6FEwoat3Q8dGDTuFYacTjDQTkBpifVcQQzoPv4YDaa-RX4M8INv44uW4CKJquSJwwv30TMWSbkSoFOJOaQ_6Q";

    @Value("${example.property}")
    private String exampleProperty;

    @BeforeEach
    public void init() {
    }

    @Test
    void contextLoads() {
    }

}
