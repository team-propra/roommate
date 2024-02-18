package com.example.roommate.tests.controller;


import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuthVerifiedUser;
import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.controller.HomeController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HomeController.class)
@TestClass
public class  HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationApplicationService authenticationApplicationService;


    @DisplayName("A GET-Request on / returns a status 200 and displays the home.html")
    @Test
    @WithMockOAuthVerifiedUser
    public void test_01() throws Exception {
        when(authenticationApplicationService.userHasKey("verified_user")).thenReturn(true);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

    }

//    @Test
//    @DisplayName("Home Seite zeigt bei vorhandenen Bookings die korrekten RaumIDs und Austattungen an")
//    @WithMockOAuth2User
//    @Disabled
//    public void test_02() throws Exception {
//        List<IRoom> rooms = EntityFactory.createRoomsWithItems();
//        Collection<IBooking> bookings = EntityFactory.createMockedBookings();
//
//        when(bookingApplicationService.getBookEntries()).thenReturn(bookings);
//
//        MvcResult mvcResult = mockMvc.perform(get("/"))
//                //.andExpect(view().name("home"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String html = mvcResult.getResponse().getContentAsString();
//        System.out.println("hmtl Antwort: " + html);
//        assertThat(html).contains("desk");
//        assertThat(html).contains("12");
//        assertThat(html).contains("13");
//        assertThat(html).contains("chair");
//        assertThat(html).contains("table");
//    }



}
