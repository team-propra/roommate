package com.example.roommate.tests.controller.booking;

import com.example.roommate.data.RoomEntry;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.persistence.RoomRepository;
import com.example.roommate.services.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class GetRoomIDTest {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    RoomService roomService;
    
    

    @Test
    @DisplayName("GET /room/{ID} with mocked service successfully yields roomDetails.html")
    void test_1() throws Exception {
        UUID roomId = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room room = new Room(roomId,"test");
        RoomEntry roomEntry = new RoomEntry(roomId, "test");
        RoomRepository r = mock(RoomRepository.class);
        roomService.roomRepository = r;
        when(roomService.roomRepository.findRoomByID(roomId)).thenReturn(roomEntry);
        when(roomService.findRoomByID(roomId)).thenReturn(room);
        MvcResult result = mvc.perform(get("/room/{ID}", roomId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains(room.getRoomnumber());
    }
}
