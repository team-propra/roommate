package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.ItemName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.UUID;

@Controller
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService is properly injected")
public class AdminController {

    private final BookingApplicationService bookingApplicationService;

    @Autowired
    public AdminController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    @AdminOnly
    @GetMapping("/edit")
    public String adminPage(Model model) {
        Collection<ItemName> itemList = bookingApplicationService.allItems();
        Collection<IRoom> roomList = bookingApplicationService.getRooms();

        model.addAttribute("itemList", itemList);
        model.addAttribute("roomList", roomList);
        return "adminEdit";
    }

    @AdminOnly
    @GetMapping("/room/{roomID}")
    public String roomOverview(Model model, @PathVariable UUID roomID) throws NotFoundException {
        IRoom roomByID = bookingApplicationService.findRoomByID(roomID);
        model.addAttribute("room", roomByID);
        return "roomOverview";
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
        bookingApplicationService.removeItem(itemName); // have to iterate through all workspaces to maintian consistency
        return adminPage(model);
    }

    @AdminOnly
    @PostMapping("/room/{roomID}/workspace/{workspaceID}/addItem/{itemName}")
    public ModelAndView addItem(@PathVariable UUID roomID, @PathVariable UUID workspaceID , @PathVariable String itemName) throws NotFoundRepositoryException {
        bookingApplicationService.addItemToRoom(workspaceID, itemName,roomID);
        String viewName = String.format("redirect:/room/%s/workspace/%s", roomID, workspaceID);
        return new ModelAndView(viewName);
    }

    @AdminOnly
    @PostMapping("/room/{roomID}/workspace/{workspaceID}/createItem")
    public ModelAndView createItem(@PathVariable UUID roomID, @PathVariable UUID workspaceID, @RequestParam String newItem) throws NotFoundRepositoryException {
        bookingApplicationService.createItem(newItem);
        return addItem(roomID, workspaceID, newItem);
    }

    @AdminOnly
    @PostMapping("/room/{roomID}/workspace/{workspaceID}/removeItem/{itemName}")
    public ModelAndView deleteItem(@PathVariable UUID roomID, @PathVariable UUID workspaceID , @PathVariable String itemName) throws NotFoundRepositoryException {
        bookingApplicationService.removeItemFromRoom(workspaceID, itemName, roomID);
        String viewName = String.format("redirect:/room/%s/workspace/%s", roomID, workspaceID);
        return new ModelAndView(viewName);
    }

    @AdminOnly
    @PostMapping("/createWorkspace")
    public String createWorkspace(Model model, @RequestParam String newWorkspace, @RequestParam UUID roomIDCreate) throws NotFoundRepositoryException, NotFoundException {
        bookingApplicationService.addWorkspace(newWorkspace, roomIDCreate);
        return roomOverview(model, roomIDCreate);
    }

    @AdminOnly
    @PostMapping("/deleteWorkspace/{workspaceID}")
    public String deleteWorkspace(Model model, @PathVariable UUID workspaceID, @RequestParam UUID roomIDDelete) throws NotFoundException {
        //bookingApplicationService.removeWorkspace(workspaceID);
        return roomOverview(model, roomIDDelete);
    }
}
