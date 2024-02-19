package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuth2User;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.controller.RoomController;
import com.example.roommate.interfaces.application.services.IAdminApplicationService;
import com.example.roommate.interfaces.application.services.IBookingApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {RoomController.class}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {AdminApplicationService.class, BookingApplicationService.class}))
@TestClass
public class GetBookTest {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    IAdminApplicationService adminApplicationService;

    @MockBean
    IBookingApplicationService bookingApplicationService;

    @Test
    @DisplayName("A Get-Request on /book returns home.html")
    @WithMockOAuth2User
    void test_3()throws Exception{

        mvc.perform(get("/rooms"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rooms"));
    }


    //vorerst noch keine Parametervalidierung, da evtl. neues Form-Objekt oder andere Informationen übergeben werden
    @Test
    @DisplayName("A GET-request on /book with the query parameters date and time stores them in the model")
    @WithMockOAuth2User
    void test_04() throws Exception{
        mvc.perform(get("/rooms")
                .param("date", "08.07.2014")
                .param("startTime", "14:30")
                .param("endTime", "15:00"))
                .andExpect(model().attributeExists("date", "startTime", "endTime"));
    }
}
