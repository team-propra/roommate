package com.example.roommate.values.forms;


import com.example.roommate.values.domain.BookingDays;
import com.example.roommate.validator.IsValidUUID;

import java.util.List;


public record BookDataForm(@IsValidUUID String roomID, /*@AssertTrue*/int stepSize, BookingDays bookingDays){

   



    public static BookDataForm addBookingsToForm(List<String> checkedDays, BookDataForm form){
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
        return form;
    }
    


}
