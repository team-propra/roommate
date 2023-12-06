package com.example.roommate.tests.controller;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.controller.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HomeController.class)
@TestClass
public class  HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("A GET-Request on /home returns a status 200 and displays the home.html")
    @Test
    public void test_01() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

    }

}
