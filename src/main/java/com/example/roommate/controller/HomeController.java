package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.models.RoomHomeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<RoomHomeModel> roomModels = bookingApplicationService.getRooms().stream()
                .filter(x-> !x.getBookdTimeframes().isEmpty())
                .map(x -> new RoomHomeModel(x.getRoomID(), x.getRoomNumber(), x.getItemNames(), DayTimeFrame.from(x.getBookdTimeframes()).convertToString()))
                .toList();
        model.addAttribute("rooms", roomModels);
        return "home";
    }

    @AdminOnly
    @GetMapping("/edit")
    public String adminPage(Model model) {
        Collection<ItemName> itemList = bookingApplicationService.getItems();
        Collection<IRoom> roomList = bookingApplicationService.getRooms();

        model.addAttribute("itemList", itemList);
        model.addAttribute("roomList", roomList);
        return "adminEdit";
    }

    @AdminOnly
    @PostMapping("/createItem")
    public String createItem(Model model, @RequestParam String newItem) {
        bookingApplicationService.createItem(newItem);
        return adminPage(model);
    }

    @AdminOnly
    @PostMapping("/deleteItem/{itemName}")
    public String deleteItem(Model model, @PathVariable String itemName) {
        //bookingApplicationService.removeItem(itemName); // have to iterate through all workspaces to maintian consistency
        return adminPage(model);
    }

}
