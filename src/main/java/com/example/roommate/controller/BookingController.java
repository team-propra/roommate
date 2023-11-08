package com.example.roommate.controller;

import com.example.roommate.domain.values.BookDataForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BookingController {



    // /book?time=1930&day=m
    // @RequestParam nutzen
    @GetMapping("/book")
    public String index() {
        return "book";
    }

    @GetMapping("/room/4")
    public String roomDetails() {
        return "roomDetails";
    }

    @PostMapping("/book")
    public RedirectView addBooking(BookDataForm form) {
        System.out.println(form);
        RedirectView view = new RedirectView("/home");
        view.setStatusCode(HttpStatus.CREATED);
        return view;
    }


}

