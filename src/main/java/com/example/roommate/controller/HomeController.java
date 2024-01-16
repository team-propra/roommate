package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookingDays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller()
public class HomeController {
    private final BookingApplicationService bookingApplicationService;
    @Autowired
    public HomeController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @GetMapping()
    public String index(Model model) {
        Collection<IBooking> bookEntries = bookingApplicationService.getBookEntries();
        List<IRoom> rooms = bookEntries.stream().map(b -> {
            try {
                return bookingApplicationService.findRoomByID(b.getRoomID());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        List<String> timeframes = new ArrayList<>();
        for (IBooking booking: bookEntries) {
            BookingDays bookingDays = booking.getBookingDays();
            timeframes.add(bookingDays.convertToString());
            System.out.println(bookingDays.convertToString());
        }
        model.addAttribute("bookings", bookEntries);
        model.addAttribute("rooms", rooms);
        model.addAttribute("timeframes", timeframes);

        return "home";

    }
}
