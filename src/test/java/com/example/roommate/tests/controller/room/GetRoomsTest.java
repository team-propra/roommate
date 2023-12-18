package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithCustomMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@TestClass
public class GetRoomsTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /rooms contains button that would direct to /rooms/add")
    @WithCustomMockUser
    public void test_01() throws Exception {

        MvcResult result =
                mvc.perform(get("/rooms")).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("/rooms/add");
    }
}
