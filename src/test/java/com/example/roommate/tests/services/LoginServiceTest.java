package com.example.roommate.tests.services;

import com.example.roommate.tests.factories.ValuesFactory;
import com.example.roommate.dtos.forms.LoginForm;
import com.example.roommate.services.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LoginServiceTest {

    @DisplayName("login with valid LoginData possible")
    @Test
    void test_() {
        LoginForm loginForm = ValuesFactory.createLoginData();
        LoginService loginService = new LoginService();
        assertThat(loginService.tryLogin(loginForm)).isTrue();
    }
}
