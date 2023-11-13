package com.example.roommate.controller.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class GetBookTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Wenn ein Get-Request auf /book ausgeführt wird, wird book.html zurückgegeben")
    void test_3()throws Exception{

        mvc.perform(get("/book"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("book"));
    }


    //vorerst noch keine Parametervalidierung, da evtl. neues Form-Objekt oder andere Informationen übergeben werden
    @Test
    @DisplayName("Ein GET-request auf /book mit Query Parametern date und time verarbeitet diese korrekt")
    void test_04() throws Exception{
        mvc.perform(get("/book")
                .param("date", "08.07.2014")
                .param("time", "14:30"))
                .andExpect(model().attributeExists("date", "time"));
    }
}
