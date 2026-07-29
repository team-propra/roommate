package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.values.models.AdminEditModel;
import com.example.roommate.values.models.RoomOverviewModel;
import com.example.roommate.values.models.UserModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.UUID;

@Controller
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService is properly injected")
public class AdminController {

    private final BookingApplicationService bookingApplicationService;
    private final AdminApplicationService adminApplicationService;

    @Autowired
    public AdminController(BookingApplicationService bookingApplicationService, AdminApplicationService adminApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.adminApplicationService = adminApplicationService;
    }

    @AdminOnly
    @GetMapping("/edit")
    public String adminPage(Model model) {
        AdminEditModel adminEditModel = bookingApplicationService.getAdminEditModel();
        model.addAttribute("itemList", adminEditModel.itemList());
        model.addAttribute("roomList", adminEditModel.roomList());
        return "adminEdit";
    }

    @AdminOnly
    @GetMapping({"/admin", "/admin/"})
    public String newAdminPage(Model model){

        return "adminOverview";
    }

    @AdminOnly
    @GetMapping({"/admin/users", "/admin/users/"} )
    public String adminUsersPage(Model model){
        model.addAttribute("users", adminApplicationService.getUsers().users());
        return "adminUsersOverview";
    }

    @AdminOnly
    @GetMapping({"/admin/editUser/{handle}" ,"/admin/editUser/{handle}/"})
    public String adminEditUserPage(Model model, @PathVariable String handle){
        UserModel userByHandle = adminApplicationService.getUserByHandle(handle);
        model.addAttribute("user", userByHandle);
        return "adminEditUser";
    }

    @AdminOnly
    @PostMapping("/admin/grantAdmin/{handle}")
    public ModelAndView grantAdmin(@PathVariable String handle) {
        adminApplicationService.grantAdmin(handle);
        return new ModelAndView("redirect:/admin/users");
    }

    @AdminOnly
    @PostMapping("/admin/revokeAdmin/{handle}")
    public ModelAndView revokeAdmin(@PathVariable String handle) {
        adminApplicationService.revokeAdmin(handle);
        return new ModelAndView("redirect:/admin/users");
    }

    @AdminOnly
    @GetMapping("/room/{roomID}")
    public String roomOverview(Model model, @PathVariable UUID roomID, Locale locale) throws NotFoundException {
        RoomOverviewModel room = bookingApplicationService.getRoomOverviewModel(roomID, locale);
        model.addAttribute("room", room);
        return "roomOverview";
    }

    @AdminOnly
    @PostMapping("/createItem")
    public ModelAndView createItem(@RequestParam String newItem) {
        bookingApplicationService.createItem(newItem);
        return new ModelAndView("redirect:/edit");
    }

    @AdminOnly
    @PostMapping("/deleteItem/{itemName}")
    public ModelAndView deleteItem(@PathVariable String itemName) {
        bookingApplicationService.removeItem(itemName);
        return new ModelAndView("redirect:/edit");
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
    public ModelAndView createItem(@PathVariable UUID roomID, @PathVariable UUID workspaceID, @RequestParam String newItem) {
        bookingApplicationService.createItem(newItem);
        String viewName = String.format("redirect:/room/%s/workspace/%s", roomID, workspaceID);
        return new ModelAndView(viewName);
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
    public ModelAndView createWorkspace(@RequestParam String newWorkspace, @RequestParam UUID roomIDCreate) throws NotFoundRepositoryException {
        bookingApplicationService.addWorkspace(newWorkspace, roomIDCreate);
        String viewName = String.format("redirect:/room/%s", roomIDCreate);
        return new ModelAndView(viewName);
    }

    @AdminOnly
    @PostMapping("/deleteWorkspace/{workspaceID}")
    public ModelAndView deleteWorkspace(@PathVariable UUID workspaceID, @RequestParam UUID roomIDDelete) throws NotFoundRepositoryException {
        bookingApplicationService.removeWorkspace(workspaceID, roomIDDelete);
        String viewName = String.format("redirect:/room/%s", roomIDDelete);
        return new ModelAndView(viewName);
    }
}
