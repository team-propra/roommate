package com.example.roommate.controller;

import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.models.RoomHomeModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller()
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService is properly injected")
public class HomeController {
    private final BookingApplicationService bookingApplicationService;
    private final AuthenticationApplicationService userApplicationService;
    @Autowired
    public HomeController(BookingApplicationService bookingApplicationService, AuthenticationApplicationService userApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.userApplicationService = userApplicationService;
    }

    @GetMapping()
    public String index(Model model, OAuth2AuthenticationToken auth) {
        if (auth == null) {
            List<RoomHomeModel> roomModels = bookingApplicationService.getRoomHomeModels();
            model.addAttribute("homeModels", roomModels);
            model.addAttribute("guest", true);
            return "home";
        }

        OAuth2User user = auth.getPrincipal();
        String login = user.getAttribute("login");

        if(!userApplicationService.tryEnsureUserKey(login))
            return "redirect:/";

        List<RoomHomeModel> roomModels = bookingApplicationService.getRoomHomeModels();
        model.addAttribute("homeModels", roomModels);
        return "home";
    }
}
