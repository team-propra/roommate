package com.example.roommate.controller;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.exceptions.GeneralDomainException;
import com.example.roommate.domain.values.BookDataForm;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import com.example.roommate.services.BookEntryService;
import com.example.roommate.services.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String index() {
        return "book";
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
            int stepSize = 60;
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

    record DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels,List<String> timeLabels){
        DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels, List<String> timeLabels) {
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
    public ModelAndView addBooking(@Validated BookDataForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("book");
            modelAndView.setStatus(HttpStatus.OK);
            return modelAndView;
        }
        System.out.println(form);

        //view.setStatusCode(HttpStatus.CREATED);

        try {
            bookEntryService.addBookEntry(form);
        } catch (GeneralDomainException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        }
        System.out.println("hitting it");
//        redirectAttributes.addFlashAttribute("successss", "Buchung erfolgreich hinzugefügt.");
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        modelAndView.setStatus(HttpStatus.valueOf(301));
        return modelAndView;
    }


}

