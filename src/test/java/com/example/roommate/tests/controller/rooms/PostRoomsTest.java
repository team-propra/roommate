package com.example.roommate.tests.controller.rooms;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.annotations.WithCustomMockUser;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.forms.BookDataForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest
@TestClass
public class PostRoomsTest {


    @Autowired
    MockMvc mvc;

    @DisplayName("")
    @Test
    @WithCustomMockUser
    void test_1() throws Exception {
        List<String> checkedDays = List.of("0-0", "0-1");
        String roomId = ValuesFactory.id.toString();

        mvc.perform(post("/rooms")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("roomId", roomId)
                        .param("checkedDays", checkedDays.toString())
                        .param("stepSize", "1")
                )
                .andExpect(redirectedUrl("/"));
    }
}
