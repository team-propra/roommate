package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public record DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels, List<String> timeLabels, List<List<Boolean>> reserved) {
    public static DayTimeFrame from(List<BookedTimeframe> bookedTimeframes){
        //Frames
        int times = 23;
        int days = 7;
        int stepSize = 60;
        List<String> dayLabels = generateDayLabels();
        List<String> timeLabels = generateTimeLabels(times, stepSize);
        List<List<Boolean>> reserved = bookedTimeframesToWeek2dMatrix(bookedTimeframes, stepSize, times);


        if (dayLabels.size() != days)
            throw new RuntimeException();

        return new DayTimeFrame(days,times,stepSize,dayLabels,timeLabels,reserved);
    }
    private static List<String> generateDayLabels() {
        return List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    }

    private static List<String> generateTimeLabels(int times, int stepSize) {
        List<String> timeLabels = new ArrayList<>();
        String result;
        LocalTime customTime = LocalTime.of(0, 0);
        for (int i = 0; i < (times * 60 / stepSize); i++) {
            result = String.format("%s - %s", customTime, customTime.plusMinutes(stepSize));
            customTime = customTime.plusMinutes(stepSize);
            timeLabels.add(result);
        }
        return timeLabels;
    }

    public static List<List<Boolean>> bookedTimeframesToWeek2dMatrix(List<BookedTimeframe> bookedTimeframes, int stepSize, int times) {
        List<List<Boolean>> reservedWeek = new ArrayList<>();
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.MONDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.TUESDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.WEDNESDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.THURSDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.FRIDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.SATURDAY).toList(),stepSize,times));
        reservedWeek.add(bookedTimeFramesToWeekDay(bookedTimeframes.stream().filter(x->x.day() == DayOfWeek.SUNDAY).toList(),stepSize,times));
        return reservedWeek;
    }
    private static List<Boolean> bookedTimeFramesToWeekDay(List<BookedTimeframe> bookedTimeframes, int stepSize,int times){
        List<Boolean> slots = new ArrayList<>();
        for (int step = 0; step < times; step++) {
            int startMinutes = step * stepSize;
            int endMinutes = step * stepSize + stepSize;
            LocalTime startTime = LocalTime.ofSecondOfDay(startMinutes* 60L);
            LocalTime endTime = LocalTime.ofSecondOfDay(endMinutes* 60L);
            boolean blocked = bookedTimeframes.stream().anyMatch(x -> hasOverlap(startTime, endTime, x));
            slots.add(blocked);
        }
        return slots;
    }

    public static boolean hasOverlap(LocalTime taskStartTime, LocalTime taskEndTime, BookedTimeframe bookedTimeframe) {
        LocalTime bookedEndTime = bookedTimeframe.startTime().plus(bookedTimeframe.duration());
        return taskStartTime.isBefore(bookedEndTime) && taskEndTime.isAfter(bookedTimeframe.startTime());
    }


    public String convertToString(){

        List<List<Boolean>> allBookingDays = reserved();

        String outpout = "";
        int count;
        int j = 0;
        for(List<Boolean> bookingList : allBookingDays){
            boolean bookingHappened = bookingList.stream().anyMatch(a -> a);
            if(!bookingHappened){
                j++;
                continue;
            }
            outpout = switch (j) {
                case 0 -> outpout + "Montag[";
                case 1 -> outpout + "Dienstag[";
                case 2 -> outpout + "Mittwoch[";
                case 3 -> outpout + "Donnerstag[";
                case 4 -> outpout + "Freitag[";
                case 5 -> outpout + "Samstag[";
                case 6 -> outpout + "Sonntag[";
                default -> outpout;
            };

            j++;

            boolean ersterDurchlauf =  true;
            for (int i = 0; i < bookingList.size(); i++) {
                LocalTime customTime = LocalTime.of(0, 0);
                count = 0;

                if (bookingList.get(i)){

                    if (bookingList.get(i + 1)){
                        i++;
                        while (bookingList.get(i)){
                            count++;
                            i++;
                        }

                        if(ersterDurchlauf){
                            outpout = outpout + String.format("%s - %s", customTime.plusMinutes((long) stepSize * (i-count-1)), customTime.plusMinutes((long) stepSize * (i-count-1) + stepSize +((long) count * stepSize)));

                        }
                        else{
                            outpout = outpout + String.format(", %s - %s", customTime.plusMinutes((long) stepSize * (i - count - 1)), customTime.plusMinutes((long) stepSize * (i - count - 1) + stepSize + ((long) count * stepSize)));
                        }
                        ersterDurchlauf = false;
                        continue;
                    }
                    if(ersterDurchlauf){
                        outpout = outpout + String.format("%s - %s", customTime.plusMinutes((long) stepSize * i), customTime.plusMinutes((long) stepSize * i + stepSize +(count * stepSize)));

                    }
                    else {
                        outpout = outpout + String.format(", %s - %s", customTime.plusMinutes((long) stepSize * i), customTime.plusMinutes((long) stepSize * i + stepSize + (count * stepSize)));
                    }
                    ersterDurchlauf = false;
                    continue;
                }

            }
            outpout = outpout + "] \n";
            System.out.println();
        }
        return  outpout;
    }
}