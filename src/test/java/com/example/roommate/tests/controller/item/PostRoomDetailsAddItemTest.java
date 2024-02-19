package com.example.roommate.tests.controller.item;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuthAdmin;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.controller.RoomController;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.values.domainValues.ItemName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@TestClass
public class PostRoomDetailsAddItemTest {


    @Autowired
    MockMvc mvc;

    @MockBean
    BookingApplicationService entryService;

    @MockBean
    RoomDomainService roomDomainService;

    @MockBean
    RoomRepository roomRepository;

    @MockBean
    ItemRepository itemRepository;

    @Test
    @DisplayName("POST /room/{roomID}/addItem/{itemName} returns status code 200")
    @WithMockOAuthAdmin
    @Disabled
    public void test_01() throws Exception {
        Room room = EntityFactory.createRoom();
        ItemName itemName = ValuesFactory.createItemName();
        roomRepository.add(room);
        itemRepository.addItem(itemName);

        String roomId = room.getRoomID().toString();
        String itemNameString = itemName.type();

        mvc.perform(post("/workspace/{roomID}/addItem/{itemName}", roomId, itemNameString)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk());
    }
}