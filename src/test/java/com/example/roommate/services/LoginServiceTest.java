package com.example.roommate.services;

import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.domain.models.values.LoginData;
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
