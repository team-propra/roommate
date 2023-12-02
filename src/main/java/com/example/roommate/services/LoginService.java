package com.example.roommate.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.dtos.forms.LoginForm;
import org.springframework.stereotype.Service;

@ApplicationService
public class LoginService {

    public boolean tryLogin(LoginForm loginForm) {
        return true;
    }

}
