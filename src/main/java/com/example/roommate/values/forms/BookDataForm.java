package com.example.roommate.values.forms;


import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.validator.IsValidUUID;

import java.util.List;


public record BookDataForm(@IsValidUUID String roomID, int stepSize){

   



    public static IntermediateBookDataForm addBookingsToForm(List<String> checkedDays, BookDataForm bookDataForm){

        BookingDays bookingDays = new BookingDays(bookDataForm.stepSize);
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
                        bookingDays.mondayBookings.add(timeIndex, true);
                        break;
                    case 1:
                        bookingDays.tuesdayBookings.add(timeIndex, true);
                        break;
                    case 2:
                        bookingDays.wednesdayBookings.add(timeIndex, true);
                        break;
                    case 3:
                        bookingDays.thursdayBookings.add(timeIndex, true);
                        break;
                    case 4:
                        bookingDays.fridayBookings.add(timeIndex, true);
                        break;
                    case 5:
                        bookingDays.saturdayBookings.add(timeIndex, true);
                        break;
                    case 6:
                        bookingDays.sundayBookings.add(timeIndex, true);
                        break;

                }
            }
        }
        return new IntermediateBookDataForm(bookDataForm,bookingDays);
    }


}
