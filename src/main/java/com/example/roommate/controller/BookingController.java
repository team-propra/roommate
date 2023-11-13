package com.example.roommate.controller;

import com.example.roommate.domain.values.BookDataForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BookingController {


    @GetMapping("/book")
    public String index() {
        return "book";
    }

    // alternativ kann auch @ModelAttribute("date") String date, @ModelAttribute("time") String time genutzt werden
    @GetMapping(path = "/book", params = {"date", "time"})
    public String changeBookings(@RequestParam String date, @RequestParam String time, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("time", time);
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

