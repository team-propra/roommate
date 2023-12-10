package com.example.roommate.domain.models.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarDay {

    public static void main(String[] args){
    /*    CalendarDay calendarDay = new CalendarDay();
        int listsize = (24 * 60) / 15;
        List<Boolean> liste = new ArrayList<>(Collections.nCopies(listsize, false));
        liste.add(0,true);
        liste.add(2, true);
        calendarDay.addBooking(liste, 15);
        System.out.println(calendarDay.bookedMinutes.toString());*/

        CalendarDay calendarDay = new CalendarDay();
        int listsize = (24 * 60) / 15;
        List<Boolean> liste = new ArrayList<>(Collections.nCopies(listsize, false));
        liste.add(0,true);
        liste.add(91, true);
        calendarDay.addBooking(liste, 15);
        System.out.println("groesse nach addbooking: " + calendarDay.bookedMinutes.size());
        System.out.println(calendarDay.bookedMinutes.toString());
        List<Boolean>  stepSizeList = calendarDay.convertToSpecificStepSize(60);
        System.out.println(stepSizeList.toString());

    }



    public List<Boolean> bookedMinutes = new ArrayList<>(Collections.nCopies(1440, false));//1 entry = 1 minute; 1440 = 1day

    public List<Boolean> convertToSpecificStepSize(int stepSize){
        int newSize = bookedMinutes.size() / stepSize;
        System.out.println(newSize);
        List<Boolean> newbookedMinutes = new ArrayList<>(Collections.nCopies(newSize, false));

            for(int i = 0;i < bookedMinutes.size();i++){
                if(bookedMinutes.get(i) == true && newbookedMinutes.get(i / stepSize) != true){
                    newbookedMinutes.set(i / stepSize, true);
                    System.out.println("adde an index: " + i /stepSize + " true");
                }
            }
        System.out.println(newbookedMinutes.size());
            return newbookedMinutes;
    }



    public void addBooking(List<Boolean> bookinglist, int stepSize){

        for(int i = 0;i < bookinglist.size();i++){
            if(bookinglist.get(i) == true){
                for(int j = 0;j < stepSize;j++){
                    bookedMinutes.set((i * stepSize) + j, true);
                }
            }
        }

    }
}
