package com.example.roommate.values.domainValues;

import com.example.roommate.utility.IterableSupport;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressFBWarnings(value = {"EI", "EI2"}, justification = "DayTimeFrame represents a part of an html contract")
public record DayTimeFrame(int days, int times, int stepSize, List<String> dayLabels, List<String> timeLabels, List<List<Boolean>> reserved) {
    public static DayTimeFrame from(Iterable<BookedTimeframe> bookedTimeframes){
        return from(bookedTimeframes, Locale.GERMAN);
    }

    public static DayTimeFrame from(Iterable<BookedTimeframe> bookedTimeframes, Locale locale){
        //Frames
        int times = 23;
        int days = 7;
        int stepSize = 60;
        List<String> dayLabels = generateDayLabels(locale);
        List<String> timeLabels = generateTimeLabels(times, stepSize);
        List<List<Boolean>> reserved = bookedTimeframesToWeek2dMatrix(IterableSupport.toList(bookedTimeframes), stepSize, times);


        if (dayLabels.size() != days)
            throw new RuntimeException();

        return new DayTimeFrame(days,times,stepSize,dayLabels,timeLabels,reserved);
    }
    private static List<String> generateDayLabels(Locale locale) {
        if (isEnglish(locale)) {
            return List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        }
        return List.of("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag");
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
        return convertToString(Locale.GERMAN);
    }

    public String convertToString(Locale locale){

        Iterable<List<Boolean>> allBookingDays = reserved();

        StringBuilder outpout = new StringBuilder();
        int count;
        int j = 0;
        for(List<Boolean> bookingList : allBookingDays){
            boolean bookingHappened = bookingList.stream().anyMatch(a -> a);
            if(!bookingHappened){
                j++;
                continue;
            }
            outpout = new StringBuilder(switch (j) {
                case 0 -> outpout + dayLabel(locale, 0) + "[";
                case 1 -> outpout + dayLabel(locale, 1) + "[";
                case 2 -> outpout + dayLabel(locale, 2) + "[";
                case 3 -> outpout + dayLabel(locale, 3) + "[";
                case 4 -> outpout + dayLabel(locale, 4) + "[";
                case 5 -> outpout + dayLabel(locale, 5) + "[";
                case 6 -> outpout + dayLabel(locale, 6) + "[";
                default -> outpout.toString();
            });

            j++;

            boolean firstIteration =  true;
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

                        if(firstIteration){
                            outpout.append(String.format("%s - %s", customTime.plusMinutes((long) stepSize * (i - count - 1)), customTime.plusMinutes((long) stepSize * (i - count - 1) + stepSize + ((long) count * stepSize))));

                        }
                        else{
                            outpout.append(String.format(", %s - %s", customTime.plusMinutes((long) stepSize * (i - count - 1)), customTime.plusMinutes((long) stepSize * (i - count - 1) + stepSize + ((long) count * stepSize))));
                        }
                        firstIteration = false;
                        continue;
                    }
                    if(firstIteration){
                        outpout.append(String.format("%s - %s", customTime.plusMinutes((long) stepSize * i), customTime.plusMinutes((long) stepSize * i + stepSize)));

                    }
                    else {
                        outpout.append(String.format(", %s - %s", customTime.plusMinutes((long) stepSize * i), customTime.plusMinutes((long) stepSize * i + stepSize)));
                    }
                    firstIteration = false;
                }

            }
            outpout.append("] \n");
        }
        return outpout.toString();
    }

    private static String dayLabel(Locale locale, int dayIndex) {
        return generateDayLabels(locale).get(dayIndex);
    }

    private static boolean isEnglish(Locale locale) {
        return Locale.ENGLISH.getLanguage().equals(locale.getLanguage());
    }
}
