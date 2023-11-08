package com.example.roommate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BookingController {

    @GetMapping("/book")
    public String index() {
        return "book";
    }

    @GetMapping("/room/4")
    public String roomDetails() {
        return "roomDetails";
    }

    @PostMapping("/book")
    public RedirectView addBooking() {
        RedirectView view = new RedirectView("/home");
        view.setStatusCode(HttpStatus.CREATED);
        return view;
    }


}

