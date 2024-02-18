package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuth2User;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.examples.Officer;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestClass
public class GetRoomIDTest {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    RoomDomainService roomDomainService;

    @MockBean
    RoomRepository roomRepository;

    @MockBean
    BookingApplicationService bookingApplicationService;

    @MockBean
    ItemRepository itemRepository;
    
    

    @Test
    @DisplayName("GET /room/{roomId}/workspace/{workspaceId} with mocked service successfully yields roomDetails.html")
    @WithMockOAuth2User
    void test_1() throws Exception {
        Room office = Officer.Room();
        Workspace officeWorkspace = Officer.Workspace();
        when(bookingApplicationService.findRoomByID(office.getRoomID())).thenReturn(office);

        MvcResult result = mvc.perform(get("/room/{roomId}/workspace/{workspaceId}", office.getRoomID().toString(), officeWorkspace.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains(office.getRoomNumber().number());
        assertThat(html).contains(String.format("%d",officeWorkspace.getWorkspaceNumber()));
    }
}
