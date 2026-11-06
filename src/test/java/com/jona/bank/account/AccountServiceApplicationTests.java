package com.jona.bank.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
class AccountServiceApplicationTests {

    static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:8.0"));

    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {

        registry.add("spring.mongodb.uri", () -> {

            if (!mongoDBContainer.isRunning()) {
                mongoDBContainer.start();
            }

            return mongoDBContainer.getReplicaSetUrl();
        });
    }

    @Test
    void contextLoads() {
    }
}