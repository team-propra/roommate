package com.example.roommate.controller.booking;


import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.services.BookEntryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.awt.print.Book;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class PostBookTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookEntryService entryService;


    UUID roomID = UUID.fromString("21f0949f-4824-45b5-be3b-a74da8be8255");


    @Test
    @DisplayName("Its NOT possible to get 201 from POST /book ")
    @Disabled
    void test_1() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID,true);
        mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(status().isCreated());
    }

    @DisplayName("POST /book redirects to /home")
    @Test
    void test_2() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID,true);
        mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(redirectedUrl("/home"));
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

    @Test
    @DisplayName("POST /book returns error1.html page when BookDataForm is not validated (f.ex.ID is blank)")

    public void test_4() throws Exception {
        BookDataForm wrongForm = new BookDataForm(null,true);

        mvc.perform(post("/book")
                //.param("roomID", "")
                //.param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("form", wrongForm.toString()))
                .andExpect(view().name("error1"))
                .andExpect(status().is(400));
    }


    //
    @Test
    @DisplayName("POST /book returns Bad-Request and 400 status if BookEntryService.addBookEntry " +
            "throws GeneralDomainiException")

    public void test_5() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID,true);
        //entryService = mock(BookEntryService.class);
        Mockito.doThrow(new GeneralDomainException()).when(entryService).addBookEntry(bookDataForm);

        mvc.perform(post("/book")
                        //.param("form", bookDataForm.toString()))
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(view().name("bad-request"))
                .andExpect(status().is(400));
    }


}
