package com.example.roommate.values.domainValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarDay {

    public List<Boolean> bookedMinutes = new ArrayList<>(Collections.nCopies(1440, false));//1 entry = 1 minute; 1440 = 1day

    public List<Boolean> convertToSpecificStepSize(int stepSize){
        int newSize = bookedMinutes.size() / stepSize;
        List<Boolean> newBookedMinutes = new ArrayList<>(Collections.nCopies(newSize, false));

            for(int i = 0;i < bookedMinutes.size();i++){
                if(bookedMinutes.get(i) && !newBookedMinutes.get(i / stepSize)){
                    newBookedMinutes.set(i / stepSize, true);
                }
            }
            return newBookedMinutes;
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
