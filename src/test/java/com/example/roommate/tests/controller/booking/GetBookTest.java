package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.TestClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@TestClass
public class GetBookTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("A Get-Request on /book returns home.html")
    @WithMockUser(username = "user", password = "1234", roles = {})
    void test_3()throws Exception{

        mvc.perform(get("/rooms"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rooms"));
    }


    //vorerst noch keine Parametervalidierung, da evtl. neues Form-Objekt oder andere Informationen übergeben werden
    @Test
    @DisplayName("A GET-request on /book with the query parameters date and time stores them in the model")
    @WithMockUser(username = "user", password = "1234", roles = {})void test_04() throws Exception{
        mvc.perform(get("/book")
                .param("date", "08.07.2014")
                .param("time", "14:30"))
                .andExpect(model().attributeExists("date", "time"));
    }
}
