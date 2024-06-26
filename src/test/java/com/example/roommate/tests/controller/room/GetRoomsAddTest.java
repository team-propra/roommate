package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestClass
public class GetRoomsAddTest {


    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /rooms/add returns status code 200")
    @WithMockOAuth2User
    public void test_01() throws Exception {
        mvc.perform(get("/rooms"))
                .andExpect(status().isOk());
    }
}

