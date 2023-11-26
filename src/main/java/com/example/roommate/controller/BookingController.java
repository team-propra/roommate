package com.example.roommate.controller;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import com.example.roommate.services.BookEntryService;
import com.example.roommate.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView roomDetails(Model model, @PathVariable UUID roomID) {
        try {
            Room roomByID = roomService.findRoomByID(roomID);
            model.addAttribute("room", roomByID);
            
            ModelAndView modelAndView = new ModelAndView("roomDetails");
            modelAndView.setStatus(HttpStatus.OK);
            return modelAndView;
        } catch (NotFoundRepositoryException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    @PostMapping("/book")
    public ModelAndView addBooking(@Validated BookDataForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("book");
            modelAndView.setStatus(HttpStatus.OK);
            return modelAndView;
        }
        System.out.println(form);

        //view.setStatusCode(HttpStatus.CREATED);

        try {
            bookEntryService.addBookEntry(form);
        } catch (GeneralDomainException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }
        System.out.println("hitting it");
//        redirectAttributes.addFlashAttribute("successss", "Buchung erfolgreich hinzugefügt.");
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        modelAndView.setStatus(HttpStatus.valueOf(301));
        return modelAndView;
    }


}

