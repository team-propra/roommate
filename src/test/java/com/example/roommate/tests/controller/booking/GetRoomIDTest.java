package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithCustomMockUser;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.services.RoomDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

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
    
    

    @Test
    @DisplayName("GET /room/{ID} with mocked service successfully yields roomDetails.html")
    @WithCustomMockUser
    void test_1() throws Exception {
        UUID roomId = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room room = new Room(roomId,"test");
        when(roomDomainService.findRoomByID(roomId)).thenReturn(room);
        MvcResult result = mvc.perform(get("/room/{ID}", roomId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains(room.getRoomNumber());
    }
}
