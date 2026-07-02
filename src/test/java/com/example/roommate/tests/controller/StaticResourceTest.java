package com.example.roommate.tests.controller;

import com.example.roommate.annotations.TestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestClass
public class StaticResourceTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /css/roommate.css serves the Roommate stylesheet")
    public void test_01() throws Exception {
        mvc.perform(get("/css/roommate.css"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(".app-nav")));
    }
}
