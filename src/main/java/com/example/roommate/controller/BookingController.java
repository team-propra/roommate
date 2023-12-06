package com.example.roommate.controller;

import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.tests.domain.exceptions.GeneralDomainException;
import com.example.roommate.dtos.forms.BookDataForm;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
import com.example.roommate.services.BookEntryService;
import com.example.roommate.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalTime;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class BookingController {

    private final BookEntryService bookEntryService;
    private final RoomService roomService;

    @Autowired
    public BookingController(BookEntryService bookEntryService, RoomService roomService) {
        this.bookEntryService = bookEntryService;
        this.roomService = roomService;
    }

    @GetMapping("/book")
    public String index(Model model) {

        model.addAttribute("items", roomService.getItems());
        model.addAttribute("rooms", roomService.getRooms());
        return "book";
    }

    @PostMapping("/filter")
    public String filterRooms(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("rooms", roomService.getRooms());
        return "redirect:/book";
    }


    // alternativ kann auch @ModelAttribute("date") String date, @ModelAttribute("time") String time genutzt werden
    @GetMapping(path = "/book", params = {"date", "time"})
    public String changeBookings(@RequestParam String date, @RequestParam String time, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        return "book";
    }

    @GetMapping("/room/{roomID}")
    public ModelAndView roomDetails(Model model, @PathVariable UUID roomID) {
        try {
            Room roomByID = roomService.findRoomByID(roomID);
            model.addAttribute("room", roomByID);
            
            //Frames
            int times = 24;
            int days = 7;
            int stepSize = 30;
            List<String> dayLabels = List.of("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
            List<String> timeLabels = new ArrayList<>();
            generateTimeLabels(days, times,stepSize, timeLabels);

            System.out.println(dayLabels.size());
            System.out.println(timeLabels.size());
            DayTimeFrame dayTimeFrame = new DayTimeFrame(days,times,stepSize,dayLabels,timeLabels);
            model.addAttribute("frame",dayTimeFrame);

//

            ModelAndView modelAndView = new ModelAndView("roomDetails");
            modelAndView.setStatus(HttpStatus.OK);
            return modelAndView;
        } catch (NotFoundRepositoryException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        }
    }

    private static void generateTimeLabels(int days, int times,int stepSize, List<String> timeLabels) {
        /*for (int day = 0; day < days; day++) {
            for (int time = 0; time < times; time++) {
                timeLabels.add(String.format("%d:%d",day,time));
            }
        }*/
        String result = "";

        LocalTime customTime = LocalTime.of(0, 0);
        for(int i = 0;i < (times* 60 / stepSize);i++){
            result = String.format("%s - %s", customTime, customTime.plusMinutes(stepSize));
            customTime = customTime.plusMinutes(stepSize);
             timeLabels.add(result);
        }
    }

    public record DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels,List<String> timeLabels){
        public DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels, List<String> timeLabels) {
            if(dayLabels.size() != days)
                throw new RuntimeException();
         //   if(timeLabels.size() != times*days)
         //       throw new RuntimeException();
            this.days = days;
            this.times = times;
            this.stepSize = stepSize;
            this.dayLabels = dayLabels;
            this.timeLabels = timeLabels;
        }
    }


    @PostMapping("/book")
    public ModelAndView addBooking(@Valid BookDataForm form
            , BindingResult bindingResult
            , RedirectAttributes redirectAttributes
            ,@RequestParam(value="cell", defaultValue = "false")List<String> checkedDays,
             @RequestParam(value="box", defaultValue = "false")List<String> boxes
    ) {
        ModelAndView modelAndView = new ModelAndView("bad-request");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;

       /* if(bindingResult.hasErrors()) {
            String id = form.roomID();
            String errorMessage = "No Room selected. Please select a room to book or return home";
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s".formatted(id));
        }
        System.out.println(form);

        for(int i = 0;i < checkedDays.size();i++){

            System.out.println("Box for " + checkedDays.get(i)+ " is " + boxes.get(i));
        }
        System.out.println("groesse: " + checkedDays.size());
        //view.setStatusCode(HttpStatus.CREATED);

        try {
            bookEntryService.addBookEntry(form);
        } catch (GeneralDomainException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");*/
    }


}

