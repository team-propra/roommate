package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.application.services.UserApplicationService;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.models.RoomHomeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {
    private final BookingApplicationService bookingApplicationService;
    private final UserApplicationService userApplicationService;
    @Autowired
    public HomeController(BookingApplicationService bookingApplicationService, UserApplicationService userApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.userApplicationService = userApplicationService;
    }

    @GetMapping()
    public String index(Model model, OAuth2AuthenticationToken auth) {
        OAuth2User user = auth.getPrincipal();
        String login = user.getAttribute("login");
        if(!userApplicationService.isVerified(login)) {
            model.addAttribute("username", login);
            return "keyForm";
        }

        List<RoomHomeModel> roomModels = bookingApplicationService.getRooms().stream()
                .filter(x-> !x.getBookedTimeframes().isEmpty())
                .map(x -> new RoomHomeModel(x.getRoomID(), x.getRoomNumber(), x.getItemNames(), DayTimeFrame.from(x.getBookedTimeframes()).convertToString()))
                .toList();
        model.addAttribute("rooms", roomModels);
        return "home";
    }
}