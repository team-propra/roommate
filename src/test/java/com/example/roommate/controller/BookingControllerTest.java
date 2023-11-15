package com.example.roommate.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die roomDetails-Seite ist unter /room/{ID} erreichbar")
    void test_1() throws Exception {
        int roomId = 4;

        MvcResult result = mvc.perform(get("/room/{ID}", roomId))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains("Details zu Arbeitsplatz " + roomId);
    }
    
    @Disabled
    @Test
    @DisplayName("Its possible to POST /book")
    void test_2() throws Exception {
        mvc.perform(post("/book"))
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Wenn ein Get-Request auf /book ausgeführt wird, wird book.html zurückgegeben")
    void test_3()throws Exception{

            mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/book"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(MockMvcResultMatchers.view().name("book"));
    }
}