package com.example.roommate.controller.booking;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PostBookTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Its possible to POST /book")
    void test_2() throws Exception {
        mvc.perform(post("/book"))
                .andExpect(status().isCreated());
    }
}
