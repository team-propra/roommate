package com.example.roommate.tests.controller.item;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuthAdmin;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.domainValues.ItemName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestClass
public class PostRoomDetailsAddItemTest {


    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("POST /room/{roomID}/addItem/{itemName} returns status code 200")
    @WithMockOAuthAdmin
    public void test_01() throws Exception {
        Room room = EntityFactory.createRoom();
        // how do I insert that into the IRoomRepository
        // I can add it to the applicationService or inject there a mocked DomainService
        // but in the Room-Controller, the Application-Service is public final
        ItemName itemName = ValuesFactory.createItemName();
        mvc.perform(get("/room/{roomID}/addItem/{itemName}"))
                .andExpect(status().isOk());
    }

    @DisplayName("POST /room/{roomID}/addItem/{itemName} adds the Item to the Room")
    @Test
    void test_() {

    }
}