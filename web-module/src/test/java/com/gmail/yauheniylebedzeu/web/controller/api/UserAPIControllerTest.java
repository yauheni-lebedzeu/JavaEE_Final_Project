package com.gmail.yauheniylebedzeu.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserAPIController.class)
public class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldAddNewUser() {

    }

}
