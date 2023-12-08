package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.values.forms.LoginForm;

@ApplicationService
public class LoginApplicationService {

    public boolean tryLogin(LoginForm loginForm) {
        return true;
    }

}
