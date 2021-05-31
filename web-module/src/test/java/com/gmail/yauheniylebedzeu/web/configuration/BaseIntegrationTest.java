package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.web.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles(value = {"it", "test"})
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private static final MySQLContainer mySqlContainer;

    static {
        mySqlContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
                .withUsername("test")
                .withPassword("1234")
                .withReuse(true);
        mySqlContainer.start();
    }

    @DynamicPropertySource
    public static void setDataSourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySqlContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySqlContainer::getPassword);
    }
}