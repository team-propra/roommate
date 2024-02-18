package com.example.roommate.controller;

import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.forms.KeyMasterForm;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    BookingApplicationService bookingApplicationService;
    AuthenticationApplicationService userApplicationService;

    @Autowired
    public ApiController(BookingApplicationService bookingApplicationService, AuthenticationApplicationService userApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.userApplicationService = userApplicationService;
    }


    @GetMapping("/api/access")
    public List<KeyMasterForm> getKeysAndRooms() {

        //String roomID = bookingApplicationService.getRooms() ...
        return IterableSupport.toList(bookingApplicationService.getAssociatedBookEntries());


    }
}