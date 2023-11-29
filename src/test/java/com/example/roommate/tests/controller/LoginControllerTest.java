package com.example.roommate.tests.controller;

import com.example.roommate.controller.LoginController;
import com.example.roommate.dtos.forms.LoginForm;
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

        when(loginService.tryLogin(new LoginForm("Otto", "1234"))).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "Otto")
                        .param("password", "1234"))
                .andExpect(redirectedUrl("/home"));
    }



    @Test
    @DisplayName("If loginservices.trylogin() returns false, a POST request on /login returns an error")
    public void test_02() throws Exception {

        when(loginService.tryLogin(new LoginForm("Otto", "1234"))).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("username", "Otto")
                        .param("password", "1234"))
                .andExpect(view().name("error1"));
    }

    @Test
    @DisplayName("A GET request on /login displays login.html")
    public void test_03() throws Exception {


        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("A GET request on /register displays register.html")
    public void test_04() throws Exception {


        mockMvc.perform(get("/register"))
                .andExpect(view().name("register"));
    }
}
