package com.example.roommate.controller;

import com.example.roommate.annotations.AdminOnly;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.*;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.values.forms.RoomDataForm;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    public RoomController(BookingApplicationService bookingApplicationService, AdminApplicationService adminApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
        this.adminApplicationService = adminApplicationService;
    }

    // http://localhost:8080/rooms?datum=1221-12-21&uhrzeit=12%3A21&gegenstaende=Table&gegenstaende=Desk
    @GetMapping("/rooms")
    public String changeBookings(@RequestParam(required = false) List<String> gegenstaende, @RequestParam(required = false) String datum, @RequestParam(required = false) String startUhrzeit, @RequestParam(required = false) String endUhrzeit, Model model) {
        if (datum == null) datum = "2024-01-01";
        if (startUhrzeit == null) startUhrzeit = "08:00";
        if (endUhrzeit == null) endUhrzeit = "16:00";
        if (gegenstaende == null) gegenstaende = new ArrayList<>();

        List<ItemName> selectedItemsList = gegenstaende.stream()
                .map(ItemName::new)
                .collect(Collectors.toList());

        model.addAttribute("date", datum);
        model.addAttribute("startTime", startUhrzeit);
        model.addAttribute("endTime", endUhrzeit);
        model.addAttribute("items", bookingApplicationService.getItems());
        model.addAttribute("gegenstaende", gegenstaende);
        model.addAttribute("rooms", bookingApplicationService.findAvailableRoomsWithItems(selectedItemsList, datum, startUhrzeit, endUhrzeit)); //findRoomsWithItem(selectedItemsList) klappt noch nicht
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

    @GetMapping("/room/{id}")
    public ModelAndView roomDetails(Model model, @PathVariable UUID id) {
        try {
            IRoom roomByID = bookingApplicationService.findRoomByID(id);
            List<String> itemStringList = IterableSupport.toList(roomByID.getItemNames())
                    .stream()
                    .map(ItemName::toString)
                    .toList();
            List<ItemName> items = IterableSupport.toList(roomByID.getItemNames());
            List<String> filteredItems = bookingApplicationService.getItems()
                    .stream()
                    .filter(item -> !items.contains(item))
                    .map(ItemName::toString)
                    .collect(Collectors.toList());

            DayTimeFrame dayTimeFrame = DayTimeFrame.from(roomByID.getBookdTimeframes());
            model.addAttribute("frame",dayTimeFrame);

            ModelAndView modelAndView = new ModelAndView("roomDetails");
            modelAndView.setStatus(HttpStatus.OK);

            model.addAttribute("room", roomByID);
            model.addAttribute("itemStringList", itemStringList);
            model.addAttribute("notSelcetedItems", filteredItems);
            return modelAndView;
        } catch (NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }


    @PostMapping("/rooms")
    public ModelAndView addBooking(@Valid BookDataForm form
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            , @RequestParam(value = "cell", defaultValue = "false") List<String> checkedDays
//             ,@RequestParam(value="box", defaultValue = "false")List<String> boxes
    ) throws ArgumentValidationException {

        if (bindingResult.hasErrors() || !BookingDays.validateBookingCoorectness(BookingDays.from(form.stepSize(),checkedDays))) {
            UUID id = form.id();
            String errorMessage = "No Room selected. Please select a room to book or return home";
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s".formatted(id));
        }

        IntermediateBookDataForm addedBookingsForm = BookDataForm.addBookingsToForm(checkedDays, form);


        try {
            bookingApplicationService.addBookEntry(addedBookingsForm);
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
