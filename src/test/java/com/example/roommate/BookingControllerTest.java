package com.example.roommate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die roomDetails-Seite ist unter /room/4 erreichbar")
    void test_1() throws Exception {
        MvcResult result = mvc.perform(get("/room/4"))
                .andExpect(status().isOk())
                //.andExpect(MockMvcResultMatchers.model().attributeExists("ID"))
                .andReturn();

        String html  = result.getResponse().getContentAsString();
        assertThat(html).contains("Details zu Arbeitsplatz 4");
    }
}
