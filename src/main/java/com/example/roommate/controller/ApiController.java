package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApiController {

    BookingApplicationService bookingApplicationService;

    @Autowired
    public ApiController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @GetMapping("/api/access")
    public String getKeysAndRooms() {
        //String roomID = bookingApplicationService.getRooms() ...
        UUID id = UUID.randomUUID();
        UUID keyId = UUID.randomUUID();

        return "{key:%s,room:%s}".formatted(keyId.toString(), id.toString());
    }
}