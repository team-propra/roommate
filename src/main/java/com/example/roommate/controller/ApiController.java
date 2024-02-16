package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.forms.KeyMasterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ApiController {

    BookingApplicationService bookingApplicationService;

    @Autowired
    public ApiController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @GetMapping("/api/access")
    public List<KeyMasterForm> getKeysAndRooms() {
        System.out.println("Ja, wurde neu gebaut!");
        //String roomID = bookingApplicationService.getRooms() ...
        UUID id = UUID.randomUUID();
        UUID keyId = UUID.fromString("9ef200d5-8d44-4a98-9650-85668d01cf8c");

        return List.of(new KeyMasterForm( id, keyId));
    }



    @PostMapping("/api/registration")
    public String registerKey(String keyId) {
        System.out.println(keyId);
        return "<h1> %s </h1>".formatted(keyId);

    }


}