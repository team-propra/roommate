package com.example.roommate.controller;

import com.example.roommate.domain.values.LoginData;
import com.example.roommate.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public String validLogin(LoginData loginData) {
        System.out.println(loginData);
        if (loginService.tryLogin(loginData)) {
            return "redirect:/home";
        }
        return "error1";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}

