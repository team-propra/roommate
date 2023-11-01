package com.example.roommate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


}

