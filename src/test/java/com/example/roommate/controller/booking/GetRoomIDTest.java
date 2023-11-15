package com.example.roommate.controller.booking;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class GetRoomIDTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die roomDetails-Seite ist unter /room/{ID} erreichbar")
    @Disabled
    void test_1() throws Exception {
        UUID roomId = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");

        MvcResult result = mvc.perform(get("/room/{ID}", roomId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains("Details zu Arbeitsplatz " + roomId);
    }
}
