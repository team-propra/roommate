package com.example.roommate.tests.controller.booking;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@TestClass
public class PostBookTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingApplicationService entryService;


    UUID roomID = UUID.fromString("21f0949f-4824-45b5-be3b-a74da8be8255");



    @DisplayName("POST /book redirects to /home")
    @Test
    void test_1() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID.toString(),true);
        mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(redirectedUrl("/"));
    }

    //future integration test
    //second mvc.perform might instantiate new services each time (so other solution necessary)
    @Disabled
    @DisplayName("Booking Details appear on the homepage after submiting the booking form")
    @Test
    void test_2() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID.toString(),true);

        MvcResult postResult = mvc.perform(post("/book")
                        .param("roomID", roomID.toString())
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andReturn();
        MvcResult getResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(postResult.getResponse().getHeader("location")).contains("/");
        String html = getResult.getResponse().getContentAsString();
        assertThat(html).contains("%s".formatted(roomID));
    }

    @Test
    @DisplayName("POST /book redirects to /room/{id} page when BookDataForm is not validated (f.ex.ID is blank)")

    public void test_3() throws Exception {
        BookDataForm wrongForm = new BookDataForm(null,true);

        mvc.perform(post("/book")
                .param("roomID", "null")
                .param("Monday19", Boolean.toString(wrongForm.Monday19())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/room/*")) ;

    }


    //
    @Test
    @DisplayName("POST /book returns Bad-Request and 400 status if BookEntryService.addBookEntry " +
            "throws GeneralDomainException")

    public void test_4() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(roomID.toString(),true);
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
