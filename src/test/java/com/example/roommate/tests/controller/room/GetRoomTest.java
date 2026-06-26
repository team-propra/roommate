package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuth2User;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.examples.Officer;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.values.models.BookingFrameModel;
import com.example.roommate.values.models.WorkspaceDetailsModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@TestClass
public class GetRoomTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingApplicationService bookingApplicationService;
    
    
    @MockBean
    RoomDomainService roomDomainService;

    @MockBean
    RoomRepository roomRepository;
    @MockBean
    ItemRepository itemRepository;
    @Test
    @DisplayName("GET /room/{id}/workspace/{workspaceId} successfully yields OK, roomNumber and workspaceNumber is present in html whenever the service returns successfully")
    @WithMockOAuth2User
    public void test_1() throws Exception {
        Room office = Officer.Room();
        Workspace officeWorkspace = Officer.Workspace();
        BookingFrameModel frame = new BookingFrameModel(
                60,
                7,
                23,
                List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                Collections.nCopies(23, ""),
                Collections.nCopies(7, Collections.nCopies(23, false))
        );
        WorkspaceDetailsModel workspaceDetails = new WorkspaceDetailsModel(
                office.getRoomID(),
                office.getRoomNumber().number(),
                officeWorkspace.getId(),
                officeWorkspace.getWorkspaceNumber(),
                List.of(),
                List.of(),
                frame
        );
        when(bookingApplicationService.getWorkspaceDetailsModel(office.getRoomID(), officeWorkspace.getId())).thenReturn(workspaceDetails);
        
        MvcResult result = mvc.perform(get("/room/{roomId}/workspace/{workspaceId}",office.getRoomID(), officeWorkspace.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(office.getRoomNumber().number());
        assertThat(result.getResponse().getContentAsString()).contains(String.format("%d",officeWorkspace.getWorkspaceNumber()));
        
    }

    @Test
    @DisplayName("GET /room/{id} successfully yields NotFound and the not-found view whenever the room doesnt exist")
    @WithMockOAuth2User
    public void test_2() throws Exception {
        when(bookingApplicationService.getWorkspaceDetailsModel(Officer.RoomID(), Officer.WorkspaceID())).thenThrow(new NotFoundException());
        mvc.perform(get("/room/{roomId}/workspace/{workspaceId}",Officer.RoomID().toString(),Officer.WorkspaceID().toString()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));

    }
}
