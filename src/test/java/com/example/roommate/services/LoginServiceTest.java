package com.example.roommate.services;

import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.domain.values.LoginData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LoginServiceTest {

    @DisplayName("can create LoginData")
    @Test
    void test_() {
        LoginData loginData = ValuesFactory.createLoginData();
        LoginService loginService = new LoginService();
        assertThat(loginService.tryLogin(loginData)).isTrue();
    }
}
