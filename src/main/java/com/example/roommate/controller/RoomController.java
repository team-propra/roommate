package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.annotations.VerifiedOnly;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.forms.RoomDataForm;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.RoomSearchModel;
import com.example.roommate.values.models.WorkspaceDetailsModel;
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

@Controller
@SuppressFBWarnings(value="EI2", justification="BookingApplicationService & AdminApplicationService are properly injected")
public class RoomController {
    private static final String GUEST_HANDLE = "__guest__";

    private final BookingApplicationService bookingApplicationService;

    private final AdminApplicationService adminApplicationService;

    @Autowired
    public RoomController(BookingApplicationService bookingApplicationService, AdminApplicationService adminApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.adminApplicationService = adminApplicationService;
    }


    @GetMapping("/rooms")
    public String changeBookings(@RequestParam(required = false) List<String> gegenstaende, SearchTimeForm timeForm, Model model,
                                 OAuth2AuthenticationToken auth) {
        String userHandle = GUEST_HANDLE;
        if (auth != null) {
            OAuth2User user = auth.getPrincipal();
            userHandle = user.getAttribute("login");
        }

        RoomSearchModel searchModel = bookingApplicationService.getRoomSearchModel(gegenstaende, timeForm, userHandle);
        model.addAttribute("date", searchModel.date());
        model.addAttribute("startTime", searchModel.startTime());
        model.addAttribute("endTime", searchModel.endTime());
        model.addAttribute("items", searchModel.items());
        model.addAttribute("gegenstaende", searchModel.selectedItems());
        model.addAttribute("roomBookingModels", searchModel.roomBookingModels());
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
            WorkspaceDetailsModel workspaceDetails = bookingApplicationService.getWorkspaceDetailsModel(roomId, workspaceId);
            model.addAttribute("workspaceDetails", workspaceDetails);
            model.addAttribute("frame", workspaceDetails.frame());
            model.addAttribute("itemStringList", workspaceDetails.selectedItems());
            model.addAttribute("notSelectedItems", workspaceDetails.notSelectedItems());

            ModelAndView modelAndView = new ModelAndView("workspaceDetails");
            modelAndView.setStatus(HttpStatus.OK);
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
        
        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }

        if (!bookingApplicationService.isBookingSelectionValid(form, checkedDays)) {
            UUID roomId = form.roomId();
            UUID workspaceId = form.workspaceId();
            String errorMessage = "No Room selected. Please select a room to book or return home";
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s/workspace/%s".formatted(roomId,workspaceId));
        }

        OAuth2User user = auth.getPrincipal();
        String userHandle = user.getAttribute("login");

        try {
            bookingApplicationService.addBookEntry(form, checkedDays, userHandle);
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
