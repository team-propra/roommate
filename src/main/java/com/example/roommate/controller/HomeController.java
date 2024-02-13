package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.models.RoomHomeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        List<RoomHomeModel> roomModels = bookingApplicationService.getRooms().stream()
                .filter(x-> !x.getBookdTimeframes().isEmpty())
                .map(x -> new RoomHomeModel(x.getRoomID(), x.getRoomNumber(), x.getItemNames(), DayTimeFrame.from(x.getBookdTimeframes()).convertToString()))
                .toList();
        model.addAttribute("rooms", roomModels);
        return "home";
    }

    @AdminOnly
    @GetMapping("/edit")
    public String adminPage() {
        return "adminEdit";
    }
}
