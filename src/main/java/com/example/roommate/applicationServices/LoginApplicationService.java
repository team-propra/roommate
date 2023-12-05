package com.example.roommate.applicationServices;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.dtos.forms.LoginForm;

@ApplicationService
public class LoginApplicationService {

    public boolean tryLogin(LoginForm loginForm) {
        return true;
    }

}
