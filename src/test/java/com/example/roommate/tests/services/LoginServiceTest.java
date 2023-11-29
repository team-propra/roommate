package com.example.roommate.tests.services;

import com.example.roommate.tests.factories.ValuesFactory;
import com.example.roommate.domain.values.LoginData;
import com.example.roommate.services.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LoginServiceTest {

    @DisplayName("login with valid LoginData possible")
    @Test
    void test_() {
        LoginData loginData = ValuesFactory.createLoginData();
        LoginService loginService = new LoginService();
        assertThat(loginService.tryLogin(loginData)).isTrue();
    }
}
