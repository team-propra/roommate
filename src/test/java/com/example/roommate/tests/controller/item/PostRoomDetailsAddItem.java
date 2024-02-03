package com.example.roommate.tests.controller.item;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithCustomMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestClass
public class PostRoomDetailsAddItem {


    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("POST /room/{roomID}/addItem/{itemName} returns status code 200")
    @WithCustomMockUser //Admin berechtigungen
    public void test_01() throws Exception {
        // add Room
        mvc.perform(get("/room/{roomID}/addItem/{itemName}"))
                .andExpect(status().isOk());
    }

    @DisplayName("POST /room/{roomID}/addItem/{itemName} adds the Item to the Room")
    @Test
    void test_() {

    }
}