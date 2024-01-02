package com.example.roommate.values.domainValues;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                    System.out.println("adde an index: " + i /stepSize + " true");
                }
            }
            return newBookedMinutes;
    }

    public boolean isAvailable(String startUhrzeit, String endUhrzeit) {
        int startIndex = convertTimeToMinutes(startUhrzeit);
        int endIndex = convertTimeToMinutes(endUhrzeit);

        return bookedMinutes.subList(startIndex, endIndex + 1).stream().allMatch(value -> !value);
    }

    public static int convertTimeToMinutes(String timeString) {
        try {
            LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            return time.getHour() * 60 + time.getMinute();
        } catch (DateTimeParseException e) {
            System.err.println("Error: Invalid time format");
            e.printStackTrace();
            return -1;
        }
    }
}
