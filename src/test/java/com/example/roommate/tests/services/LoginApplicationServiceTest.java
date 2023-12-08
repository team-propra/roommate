package com.example.roommate.tests.services;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.forms.LoginForm;
import com.example.roommate.applicationServices.LoginApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@TestClass
public class LoginApplicationServiceTest {

    @DisplayName("login with valid LoginData possible")
    @Test
    void test_() {
        LoginForm loginForm = ValuesFactory.createLoginData();
        LoginApplicationService loginApplicationService = new LoginApplicationService();
        assertThat(loginApplicationService.tryLogin(loginForm)).isTrue();
    }
}
