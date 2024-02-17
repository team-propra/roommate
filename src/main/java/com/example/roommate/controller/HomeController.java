package com.example.roommate.controller;

import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.models.RoomHomeModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller()
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService is properly injected")
public class HomeController {
    private final BookingApplicationService bookingApplicationService;
    @Autowired
    public HomeController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @GetMapping()
    public String index(Model model) {
        List<RoomHomeModel> roomModels = bookingApplicationService.getRooms().stream()
                .filter(x-> !IterableSupport.toList(x.getBookedTimeframes()).isEmpty())
                .flatMap(room->IterableSupport.toList(room.getWorkspaces()).stream()
                        .map(workspace->new RoomHomeModel(room.getRoomID(),
                                workspace.getId(),
                                room.getRoomNumber(),
                                workspace.getWorkspaceNumber(),
                                DayTimeFrame.from(IterableSupport.toList(room.getBookedTimeframes())).convertToString(),
                                workspace.getItems()
                        )))
                .toList();
        model.addAttribute("homeModels", roomModels);
        return "home";
    }
}
