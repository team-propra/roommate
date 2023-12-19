package com.example.roommate.values.domain;

import java.util.ArrayList;
import java.util.List;

public record CalendarDays(
        CalendarDay monday,
        CalendarDay tuesday,
        CalendarDay wednesday,
        CalendarDay thursday,
        CalendarDay friday,
        CalendarDay saturday,
        CalendarDay sunday
) {
    public CalendarDays() {
        this(
                new CalendarDay(),
                new CalendarDay(),
                new CalendarDay(),
                new CalendarDay(),
                new CalendarDay(),
                new CalendarDay(),
                new CalendarDay()
        );
    }

    public static List<List<Boolean>> convertRoomCalendarDaysTo2dMatrix(CalendarDays calendarDays, int stepSize) {
        List<Boolean> convertedMonday = calendarDays.monday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedTuesday = calendarDays.tuesday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedWednesday = calendarDays.wednesday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedThursday = calendarDays.thursday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedFriday = calendarDays.friday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedSaturday = calendarDays.saturday().convertToSpecificStepSize(stepSize);
        List<Boolean> convertedSunday = calendarDays.sunday().convertToSpecificStepSize(stepSize);

        List<List<Boolean>> reservedSlots = new ArrayList<>();
        reservedSlots.add(convertedMonday);
        reservedSlots.add(convertedTuesday);
        reservedSlots.add(convertedWednesday);
        reservedSlots.add(convertedThursday);
        reservedSlots.add(convertedFriday);
        reservedSlots.add(convertedSaturday);
        reservedSlots.add(convertedSunday);

        return  reservedSlots;
    }
}
