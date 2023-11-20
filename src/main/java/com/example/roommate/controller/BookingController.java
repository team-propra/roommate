package com.example.roommate.controller;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import com.example.roommate.services.BookEntryService;
import com.example.roommate.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.UUID;

@Controller
public class BookingController {

    private final BookEntryService bookEntryService;
    private final RoomService roomService;

    @Autowired
    public BookingController(BookEntryService bookEntryService, RoomService roomService) {
        this.bookEntryService = bookEntryService;
        this.roomService = roomService;
    }

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

    @GetMapping("/room/{roomID}")
    public String roomDetails(Model model, @PathVariable UUID roomID) {
        try {
            Room roomByID = roomService.findRoomByID(roomID);
            model.addAttribute("room", roomByID);
            return "roomDetails";
        } catch (NotFoundRepositoryException e) {
            return "not-found";
        }
    }

    @PostMapping("/book")
    public String addBooking( @Validated BookDataForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "book";
        }
        System.out.println(form);

        //view.setStatusCode(HttpStatus.CREATED);

        try {
            bookEntryService.addBookEntry(form);
        } catch (GeneralDomainException e) {
            return "error";
        }
        redirectAttributes.addFlashAttribute("success", "Buchung erfolgreich hinzugefügt.");
        return "redirect:/home";
    }


}

