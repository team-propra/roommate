package com.example.roommate.controller;

import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.forms.KeyMasterForm;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService & AuthenticationApplicationService are properly injected")
public class ApiController {

    BookingApplicationService bookingApplicationService;

    @Autowired
    public ApiController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @GetMapping("/api/access")
    public List<KeyMasterForm> getKeysAndRooms() {

        return IterableSupport.toList(bookingApplicationService.getAssociatedBookEntries());


    }
}