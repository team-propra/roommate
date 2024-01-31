package com.example.roommate.values.forms;


import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.validator.IsValidUUID;

import java.util.List;


public record BookDataForm(@IsValidUUID String id, int stepSize){

   



    public static IntermediateBookDataForm addBookingsToForm(List<String> checkedDays, BookDataForm bookDataForm){

        BookingDays bookingDays = new BookingDays(bookDataForm.stepSize);
        for (String checkedDay : checkedDays) {

            if(checkedDay.contains("-X")) {
                String[] daytime = checkedDay.split("-");

                int timeIndex = Integer.parseInt(daytime[0]);
                int day = Integer.parseInt(daytime[1]);//0=monday, 1=tuesday...
                switch(day){
                    case 0:
                        bookingDays.mondayBookings.set(timeIndex, true);
                        break;
                    case 1:
                        bookingDays.tuesdayBookings.set(timeIndex, true);
                        break;
                    case 2:
                        bookingDays.wednesdayBookings.set(timeIndex, true);
                        break;
                    case 3:
                        bookingDays.thursdayBookings.set(timeIndex, true);
                        break;
                    case 4:
                        bookingDays.fridayBookings.set(timeIndex, true);
                        break;
                    case 5:
                        bookingDays.saturdayBookings.set(timeIndex, true);
                        break;
                    case 6:
                        bookingDays.sundayBookings.set(timeIndex, true);
                        break;

                }
            }
        }
        return new IntermediateBookDataForm(bookDataForm,bookingDays);
    }
    


}
