package com.example.roommate.controller.booking;


import com.example.roommate.domain.values.BookDataForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PostBookTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("Its possible to POST /book")
    void test_2() throws Exception {
        BookDataForm bookDataForm = new BookDataForm(5,true);
        mvc.perform(post("/book")
                        .param("roomID", Integer.toString(bookDataForm.roomID()))
                        .param("Monday19", Boolean.toString(bookDataForm.Monday19())))
                .andExpect(status().isCreated());
    }
}
