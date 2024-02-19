package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record BookedTimeframe(DayOfWeek day, LocalTime startTime, Duration duration, String userHandle) {
    public boolean intersects(BookedTimeframe other) {
        if (!this.day.equals(other.day)) {
            return false;
        }

        LocalTime thisEndTime = this.startTime.plus(this.duration);
        LocalTime otherEndTime = other.startTime.plus(other.duration);

        boolean x = thisEndTime.isBefore(other.startTime) || thisEndTime.equals(other.startTime);
        boolean y = otherEndTime.isBefore(this.startTime) || otherEndTime.equals(this.startTime);


        return !(x || y);
    }
}
