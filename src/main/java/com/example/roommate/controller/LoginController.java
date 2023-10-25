package com.example.roommate.controller;

import com.example.roommate.domain.values.LoginData;
import com.example.roommate.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public @ResponseBody String validLogin(LoginData loginData) {
        System.out.println(loginData);
        if (loginService.tryLogin(loginData)) {
            return "<h1>Succses!!</h1>";
        }
        return "<h1>Error</h1>";
    }
}

