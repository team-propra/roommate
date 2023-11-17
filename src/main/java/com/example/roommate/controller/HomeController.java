package com.example.roommate.controller;

import com.example.roommate.services.BookEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
@RequestMapping("/home")
public class HomeController {
    private final BookEntryService bookEntryService;
    @Autowired
    public HomeController(BookEntryService bookEntryService) {
        this.bookEntryService = bookEntryService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("forms", bookEntryService.getBookEntries());
        return "home";
    }
}
