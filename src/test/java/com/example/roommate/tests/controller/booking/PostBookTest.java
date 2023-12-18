package com.example.roommate.tests.controller.booking;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.persistence.data.RoomEntry;
import com.example.roommate.persistence.repositories.RoomRepository;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.example.roommate.values.domain.BookingDays;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.roommate.annotations.WithCustomMockUser;
@WebMvcTest
@TestClass
public class PostBookTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingApplicationService entryService;

    @MockBean
    RoomDomainService roomDomainService;

    @MockBean
     RoomRepository roomRepository;




    UUID roomID = UUID.fromString("21f0949f-4824-45b5-be3b-a74da8be8255");

    BookDataForm bookDataForm = new BookDataForm(roomID.toString(),60,new BookingDays(60));

    @DisplayName("POST /rooms redirects to /")
    @Test
    @WithCustomMockUser
    void test_1() throws Exception {
        //BookDataForm bookDataForm = mock(BookDataForm.class);
        //RoomRepository r = Mockito.mock(RoomRepository.class);
        entryService.roomDomainService = roomDomainService;

        roomRepository.add(new RoomEntry(roomID, "randomroomnumber"));
        //when(r.findRoomByID(roomID)).thenReturn(new RoomEntry(roomID, "randomroomnumber"));
        when(roomDomainService.addBookingsToForm(new ArrayList<>(), bookDataForm)).thenReturn(bookDataForm);
        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("roomID", roomID.toString())
                       // .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(redirectedUrl("/"));
    }

    //future integration test
    //second mvc.perform might instantiate new services each time (so other solution necessary)
    @Disabled
    @DisplayName("Booking Details appear on the homepage after submiting the booking form")
    @Test
    void test_2() throws Exception {
     //   BookDataForm bookDataForm = new BookDataForm(roomID.toString(),true);

        MvcResult postResult = mvc.perform(post("/rooms")
                        .param("roomID", roomID.toString())
                       // .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                        .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andReturn();
        MvcResult getResult = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(postResult.getResponse().getHeader("location")).contains("/");
        String html = getResult.getResponse().getContentAsString();
        assertThat(html).contains("%s".formatted(roomID));
    }

    @Test
    @DisplayName("POST /rooms redirects to /room/{id} page when BookDataForm is not validated (f.ex.ID is blank)")
    @WithCustomMockUser
    public void test_3() throws Exception {
        BookDataForm wrongForm = new BookDataForm(null,60, new BookingDays(60));

        mvc.perform(post("/rooms")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("roomID", "null")
               // .param("Monday19", Boolean.toString(wrongForm.Monday19())))
                .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/room/*")) ;

    }


    //
    @Test
    @DisplayName("POST /rooms returns Bad-Request and 400 status if BookEntryService.addBookEntry " +
            "throws GeneralDomainException")
    @WithCustomMockUser
    public void test_4() throws Exception {
      //  BookDataForm bookDataForm = new BookDataForm(roomID.toString(),true);
        //entryService = mock(BookEntryService.class);
        //Mockito.doThrow(new GeneralDomainException()).when(entryService).addBookEntry(bookDataForm);
        Mockito.doThrow(new GeneralDomainException()).when(entryService).addBookEntry(Mockito.any());

        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                       // .param("form", bookDataForm.toString()))
                          .param("roomID", roomID.toString())
                        //   .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                         .param("stepSize", String.valueOf(bookDataForm.stepSize())))
                .andExpect(view().name("bad-request"))
                .andExpect(status().is(400));
    }
}
