package com.example.roommate.controller;

import com.example.roommate.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
@RequestMapping("/home")
public class HomeController {
    private final BookingService bookingService;
    @Autowired
    public HomeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("forms", bookingService.getBookEntries());
        return "home";
    }
}
