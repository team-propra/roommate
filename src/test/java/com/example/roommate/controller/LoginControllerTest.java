package com.example.roommate.controller;

import com.example.roommate.controller.LoginController;
import com.example.roommate.domain.values.LoginData;
import com.example.roommate.services.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoginService loginService;

    @Test
    @DisplayName("Ein POST request auf /login löst einen redirect auf /home wenn loginservices.trylogin() true zurückgibt")
    public void test_01() throws Exception {

        when(loginService.tryLogin(new LoginData("Otto", "1234"))).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "Otto")
                        .param("password", "1234"))
                .andExpect(redirectedUrl("/home"));
    }



    @Test
    @DisplayName("Ein POST request auf /login löst, wenn loginservices.trylogin() false zurückgibt")
    public void test_02() throws Exception {

        when(loginService.tryLogin(new LoginData("Otto", "1234"))).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("username", "Otto")
                        .param("password", "1234"))
                .andExpect(view().name("error1"));
    }

}
