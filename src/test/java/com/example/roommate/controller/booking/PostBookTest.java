package com.example.roommate.controller.booking;


import com.example.roommate.domain.values.BookDataForm;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
public class PostBookTest {

    @Autowired
    MockMvc mvc;

    UUID roomID = UUID.fromString("21f0949f-4824-45b5-be3b-a74da8be8255");


    @Test
    @DisplayName("Its possible to POST /book")
    void test_1() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID,true);
        mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(status().isCreated());
    }

    @Disabled
    @DisplayName("POST /book redirects to /home")
    @Test
    void test_2() throws Exception {
        mvc.perform(post("/book"))
                .andExpect(view().name("home"));
    }

    @Disabled
    @DisplayName("Booking Details appear on the homepage after submiting the booking form")
    @Test
    void test_3() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID,true);
        MvcResult result = mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains("Monday19");

    }
}
