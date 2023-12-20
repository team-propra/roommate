package com.example.roommate.values.domainValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarDay {

    public List<Boolean> bookedMinutes = new ArrayList<>(Collections.nCopies(1440, false));//1 entry = 1 minute; 1440 = 1day

    public List<Boolean> convertToSpecificStepSize(int stepSize){
        int newSize = bookedMinutes.size() / stepSize;
        System.out.println(newSize);
        List<Boolean> newBookedMinutes = new ArrayList<>(Collections.nCopies(newSize, false));

            for(int i = 0;i < bookedMinutes.size();i++){
                if(bookedMinutes.get(i) && !newBookedMinutes.get(i / stepSize)){
                    newBookedMinutes.set(i / stepSize, true);
                    System.out.println("adde an index: " + i /stepSize + " true");
                }
            }
        System.out.println(newBookedMinutes.size());
            return newBookedMinutes;
    }
}
