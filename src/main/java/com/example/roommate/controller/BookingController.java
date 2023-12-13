package com.example.roommate.controller;

import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.domain.ItemName;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.application.services.BookingApplicationService;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookingController {

    private final BookingApplicationService bookingApplicationService;

    @Autowired
    public BookingController(BookingApplicationService bookingApplicationService) {
        this.bookingApplicationService = bookingApplicationService;
    }

    // http://localhost:8080/book?datum=1221-12-21&uhrzeit=12%3A21&gegenstaende=Table&gegenstaende=Desk
    @GetMapping("/book")
    public String changeBookings(@RequestParam(required = false) List<String> gegenstaende, @RequestParam(required = false) String datum, @RequestParam(required = false) String uhrzeit, Model model) {
        if (datum == null) datum = "2024-01-01";
        if (uhrzeit == null) uhrzeit = "08:00";
        if (gegenstaende == null) gegenstaende = new ArrayList<>();

        List<ItemName> selectedItemsList = gegenstaende.stream()
                .map(ItemName::new)
                .collect(Collectors.toList());

        model.addAttribute("date", datum);
        model.addAttribute("time", uhrzeit);
        model.addAttribute("items", bookingApplicationService.getItems());
        model.addAttribute("gegenstaende", gegenstaende);
        model.addAttribute("rooms", bookingApplicationService.findRoomsWithItems(selectedItemsList)); //findRoomsWithItem(selectedItemsList) klappt noch nicht
        return "book";
    }






    @GetMapping("/room/{roomID}")
    public ModelAndView roomDetails(Model model, @PathVariable UUID roomID) {
        try {
            IRoom roomByID = bookingApplicationService.findRoomByID(roomID);
            model.addAttribute("room", roomByID);
            
            //Frames
            int times = 24;
            int days = 7;
            int stepSize = 60;
            Room roomEntry = (Room) bookingApplicationService.roomDomainService.roomRepository.findRoomByID(roomID);

            List<Boolean> convertedMonday = roomEntry.monday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedTuesday = roomEntry.tuesday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedWednesday = roomEntry.wednesday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedThursday = roomEntry.thursday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedFriday = roomEntry.friday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedSaturday = roomEntry.saturday.convertToSpecificStepSize(stepSize);
            List<Boolean> convertedSunday = roomEntry.sunday.convertToSpecificStepSize(stepSize);

//            RoomEntry roomEntry = roomService.roomRepository.findRoomByID(roomID);//old
//            List<Boolean> convertedMonday = roomEntry.monday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedTuesday = roomEntry.tuesday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedWednesday = roomEntry.wednesday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedThursday = roomEntry.thursday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedFriday = roomEntry.friday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedSaturday = roomEntry.saturday().convertToSpecificStepSize(stepSize);
//            List<Boolean> convertedSunday = roomEntry.sunday().convertToSpecificStepSize(stepSize);


            List<List<Boolean>> reservedSlots = new ArrayList<>();
            reservedSlots.add(convertedMonday);
            reservedSlots.add(convertedTuesday);
            reservedSlots.add(convertedWednesday);
            reservedSlots.add(convertedThursday);
            reservedSlots.add(convertedFriday);
            reservedSlots.add(convertedSaturday);
            reservedSlots.add(convertedSunday);
            model.addAttribute("reservedSlots", reservedSlots);
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
        } catch (NotFoundException e) {
            ModelAndView modelAndView = new ModelAndView("not-found");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            return modelAndView;
        } catch (NotFoundRepositoryException e) {
            throw new RuntimeException(e);
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
        for(int i = 0;i < (times * 60 / stepSize);i++){
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


        if(bindingResult.hasErrors()) {
            String id = form.roomID();
            String errorMessage = "No Room selected. Please select a room to book or return home";
            redirectAttributes.addFlashAttribute("formValidationErrorText", errorMessage);
            return new ModelAndView("redirect:/room/%s".formatted(id));
        }
        System.out.println(form);

        for (String checkedDay : checkedDays) {

            if(checkedDay.contains("-X")) {
                String[] daytime = checkedDay.split("-");
                System.out.println("Zeile: " + daytime[0]);
                System.out.println("Tag: " + daytime[1]);
                System.out.println("Checked day " + checkedDay);

                int timeIndex = Integer.parseInt(daytime[0]);
                int day = Integer.parseInt(daytime[1]);//0=monday, 1=tuesday...
                switch(day){
                    case 0:
                        form.bookingDays().mondayBookings.add(timeIndex, true);
                        break;
                    case 1:
                        form.bookingDays().tuesdayBookings.add(timeIndex, true);
                        break;
                    case 2:
                        form.bookingDays().wednesdayBookings.add(timeIndex, true);
                        break;
                    case 3:
                        form.bookingDays().thursdayBookings.add(timeIndex, true);
                        break;
                    case 4:
                        form.bookingDays().fridayBookings.add(timeIndex, true);
                        break;
                    case 5:
                        form.bookingDays().saturdayBookings.add(timeIndex, true);
                        break;
                    case 6:
                        form.bookingDays().sundayBookings.add(timeIndex, true);
                        break;

                }
            }
        }
       // System.out.println("Stepsize: " + form.stepSize());
       // System.out.println("Stepsize von Bookingdays: " + form.bookingDays().stepsize);
       // System.out.println("Inhalt von mondaylist " + form.bookingDays().mondayBookings.toString());
       // System.out.println("größe von mondaylist " + form.bookingDays().mondayBookings.size());
//        System.out.println("groesse: " + checkedDays.size());
        //view.setStatusCode(HttpStatus.CREATED);

        try {
            bookingApplicationService.addBookEntry(form);
            bookingApplicationService.roomDomainService.addBooking(form);
        } catch (GeneralDomainException e) {
            ModelAndView modelAndView = new ModelAndView("bad-request");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:/home");
    }


}

