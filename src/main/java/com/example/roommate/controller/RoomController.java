package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.annotations.VerifiedOnly;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.application.services.KeyMasterService;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.*;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.forms.RoomDataForm;
import com.example.roommate.values.models.RoomBookingModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService & AdminApplicationService are properly injected")
public class RoomController {

    private final BookingApplicationService bookingApplicationService;

    private final AdminApplicationService adminApplicationService;

    private final KeyMasterService keyMasterService;

    @Autowired
    public RoomController(BookingApplicationService bookingApplicationService, AdminApplicationService adminApplicationService, KeyMasterService keyMasterService) {
        this.bookingApplicationService = bookingApplicationService;
        this.adminApplicationService = adminApplicationService;
        this.keyMasterService = keyMasterService;
    }


    // http://localhost:8080/rooms?datum=1221-12-21&uhrzeit=12%3A21&gegenstaende=Table&gegenstaende=Desk

    @GetMapping("/rooms")
    public String changeBookings(@RequestParam(required = false) List<String> gegenstaende,
                                 @RequestParam(required = false) String datum,
                                 @RequestParam(required = false) String startUhrzeit, @RequestParam(required = false) String endUhrzeit,
                                 Model model,
                                 OAuth2AuthenticationToken auth) {
        if (datum == null) datum = "2024-01-01";
        if (startUhrzeit == null) startUhrzeit = "08:00";
        if (endUhrzeit == null) endUhrzeit = "16:00";
        if (gegenstaende == null) gegenstaende = new ArrayList<>();

        List<ItemName> selectedItemsList = gegenstaende.stream()
                .map(ItemName::new)
                .toList();

        OAuth2User user = auth.getPrincipal();
        String userHandle = user.getAttribute("login");

        List<RoomBookingModel> availableWorkspacesWithItems = bookingApplicationService.findAvailableWorkspacesWithItems(selectedItemsList, datum, startUhrzeit, endUhrzeit, userHandle);
        model.addAttribute("date", datum);
        model.addAttribute("startTime", startUhrzeit);
        model.addAttribute("endTime", endUhrzeit);
        model.addAttribute("items", bookingApplicationService.allItems());
        model.addAttribute("gegenstaende", gegenstaende);
        model.addAttribute("roomBookingModels", availableWorkspacesWithItems);
        return "rooms";
    }

    @AdminOnly
    @GetMapping("/rooms/add")
    public String addRoomForm() {
        return "addRooms";
    }

    @AdminOnly
    @PostMapping("/rooms/add")
    public String addRoom(RoomDataForm roomDataForm){
        adminApplicationService.addRoom(roomDataForm);
        return "addRooms";
    }

    @GetMapping("/room/{roomId}/workspace/{workspaceId}")
    public ModelAndView roomDetails(Model model, @PathVariable UUID roomId, @PathVariable UUID workspaceId) {
        try {
            IRoom room = bookingApplicationService.findRoomByID(roomId);
            Optional<? extends IWorkspace> optionalWorkspace = IterableSupport.toList(room.getWorkspaces()).stream()
                    .filter(x -> x.getId().equals(workspaceId))
                    .findFirst();
            if(optionalWorkspace.isEmpty())
                throw new NotFoundException();
            IWorkspace workspace = optionalWorkspace.get();
            List<String> itemsOfWorkspace = workspace.getItems().stream().map(ItemName::type).collect(Collectors.toList());
            List<String> filteredItems = bookingApplicationService.allItems()
                    .stream()
                    .map(ItemName::type)
                    .filter(type -> !itemsOfWorkspace.contains(type))
                    .toList();
            DayTimeFrame dayTimeFrame = DayTimeFrame.from(workspace.getBookedTimeframes());
            model.addAttribute("frame",dayTimeFrame);

            ModelAndView modelAndView = new ModelAndView("workspaceDetails");
            modelAndView.setStatus(HttpStatus.OK);

            model.addAttribute("room", room);
            model.addAttribute("workspace", workspace);
            model.addAttribute("itemStringList", itemsOfWorkspace);
            model.addAttribute("notSelectedItems", filteredItems);
            return modelAndView;
        } catch (NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    @VerifiedOnly
    @PostMapping("/rooms")
    public ModelAndView addBooking(@Valid BookDataForm form
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , @RequestParam(value = "cell", defaultValue = "false") List<String> checkedDays
//             ,@RequestParam(value="box", defaultValue = "false")List<String> boxes
            , OAuth2AuthenticationToken auth
    ) throws ArgumentValidationException {

        if (bindingResult.hasErrors() || !BookingDays.validateBookingCoorectness(BookingDays.from(form.stepSize(),checkedDays))) {
            UUID roomId = form.roomId();
            UUID workspaceId = form.workspaceId();
            String errorMessage = "No Room selected. Please select a room to book or return home";
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s/workspace/%s".formatted(roomId,workspaceId));
        }

        IntermediateBookDataForm addedBookingsForm = BookDataForm.addBookingsToForm(checkedDays, form);

        OAuth2User user = auth.getPrincipal();
        String userHandle = user.getAttribute("login");

        try {
            bookingApplicationService.addBookEntry(addedBookingsForm, userHandle);
        } catch (GeneralDomainException | NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:/");
    }
}
