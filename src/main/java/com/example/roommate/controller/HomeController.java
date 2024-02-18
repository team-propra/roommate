package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.application.services.UserApplicationService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.models.RoomHomeModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@Controller()
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService is properly injected")
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

        if(!userApplicationService.userHasKey(login)) {
            model.addAttribute("username", login);
            return "keyForm";
        }

        /*
        diesen Check verlagern an Raumbuchung
        if(!userApplicationService.isVerified(login)) {
            model.addAttribute("username", login);
            return "keyForm";
        }

         */

        List<RoomHomeModel> roomModels = bookingApplicationService.getRooms().stream()
                .flatMap(HomeController::toRoomHomeModel)
                .toList();
        model.addAttribute("homeModels", roomModels);
        return "home";
    }

    private static Stream<RoomHomeModel> toRoomHomeModel(IRoom room){
        List<RoomHomeModel> list = IterableSupport.toList(room.getWorkspaces()).stream()
                .filter(workspace -> !IterableSupport.toList(workspace.getBookedTimeframes()).isEmpty())
                .map(workspace -> new RoomHomeModel(room.getRoomID(),
                        workspace.getId(),
                        room.getRoomNumber(),
                        workspace.getWorkspaceNumber(),
                        DayTimeFrame.from(IterableSupport.toList(workspace.getBookedTimeframes())).convertToString(),
                        workspace.getItems()
                ))
                .toList();
        return list.stream();
    }

    @PostMapping("/registration")
    public String registerKey(String keyId, OAuth2AuthenticationToken auth, Model model) {
        OAuth2User user = auth.getPrincipal();
        String login = user.getAttribute("login");
        UUID keyID = UUID.fromString(keyId);

        userApplicationService.registerKey(keyID, login);
        model.addAttribute("keyID", keyId);
        return "keyForm";

    }
}