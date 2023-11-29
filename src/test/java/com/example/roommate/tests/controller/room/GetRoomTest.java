package com.example.roommate.tests.controller.room;

import com.example.roommate.tests.domain.entities.Room;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
public class GetRoomTest {

    @Autowired
    MockMvc mvc;
    
    
    @MockBean
    RoomService roomService;
    @Test
    @DisplayName("GET /room/{id} successfully yields OK and room number is present in html whenever the service returns successfully")
    public void test_1() throws Exception {
        UUID roomID = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room room = new Room(roomID,"test");
        when(roomService.findRoomByID(roomID)).thenReturn(room);
        
        MvcResult result = mvc.perform(get("/room/{id}",roomID.toString()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(room.getRoomnumber());
        
    }

    @Test
    @DisplayName("GET /room/{id} successfully yields NotFound and the not-found view whenever the room doesnt exist")
    public void test_2() throws Exception {
        UUID roomID = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room goodRoom = new Room(roomID,"test-room-123");
        
        when(roomService.findRoomByID(goodRoom.getRoomID())).thenThrow(new NotFoundRepositoryException());
        
        mvc.perform(get("/room/{id}",roomID.toString()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
                
        

    }
}
