package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.annotations.WithCustomMockUser;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.persistence.repositories.ItemRepository;
import com.example.roommate.persistence.repositories.RoomRepository;
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
    @Test
    @DisplayName("GET /room/{id} successfully yields OK and room number is present in html whenever the service returns successfully")
    @WithCustomMockUser
    public void test_1() throws Exception {
        UUID roomID = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room room = new Room(roomID,"test");
        RoomRepository roomRepository = mock(RoomRepository.class);
        ItemRepository itemRepository = mock(ItemRepository.class);
        RoomDomainService roomDomainService1 = new RoomDomainService(roomRepository, itemRepository);

        bookingApplicationService.roomDomainService = roomDomainService1;
        when(roomRepository.findRoomByID(roomID)).thenReturn(room);
       // when(roomDomainService.findRoomByID(roomID)).thenReturn(room);
        when(bookingApplicationService.findRoomByID(roomID)).thenReturn(room);
        
        MvcResult result = mvc.perform(get("/room/{id}",roomID.toString()))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(room.getRoomNumber());
        
    }

    @Test
    @DisplayName("GET /room/{id} successfully yields NotFound and the not-found view whenever the room doesnt exist")
    @WithCustomMockUser
    public void test_2() throws Exception {
        UUID roomID = UUID.fromString("3c857752-79ed-4fde-a916-770ae34e70e1");
        Room goodRoom = new Room(roomID,"test-room-123");

        RoomRepository roomRepository = mock(RoomRepository.class);
        ItemRepository itemRepository = mock(ItemRepository.class);
        RoomDomainService roomDomainService1 = new RoomDomainService(roomRepository, itemRepository);

        bookingApplicationService.roomDomainService = roomDomainService1;
        when(bookingApplicationService.findRoomByID(roomID)).thenThrow(new NotFoundException());
        //when(roomRepository.findRoomByID(roomID)).thenReturn(goodRoom);
        //when(roomDomainService.findRoomByID(goodRoom.getRoomID())).thenThrow(new NotFoundRepositoryException());
        //when(bookingApplicationService.findRoomByID(roomID)).thenReturn(goodRoom);
        
        mvc.perform(get("/room/{id}",roomID.toString()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
                
        

    }
}
