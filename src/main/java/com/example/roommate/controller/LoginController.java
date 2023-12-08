package com.example.roommate.controller;

import com.example.roommate.values.forms.LoginForm;
import com.example.roommate.applicationServices.LoginApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    LoginApplicationService loginApplicationService;
    @Autowired
    public LoginController(LoginApplicationService loginApplicationService) {
        this.loginApplicationService = loginApplicationService;
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public String validLogin(LoginForm loginForm) {
        System.out.println(loginForm);
        if (loginApplicationService.tryLogin(loginForm)) {
            return "redirect:/home";
        }
        return "error1";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}

