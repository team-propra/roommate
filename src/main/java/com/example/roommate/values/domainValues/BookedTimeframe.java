package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record BookedTimeframe(DayOfWeek day, LocalTime startTime, Duration duration) {
    public boolean intersects(BookedTimeframe other) {
        if (this.day != other.day) {
            return false;
        }

        LocalTime thisEndTime = this.startTime.plus(this.duration);
        LocalTime otherEndTime = other.startTime.plus(other.duration);

        return exclusiveOr(thisEndTime.isBefore(other.startTime),otherEndTime.isBefore(this.startTime));
    }

    private boolean exclusiveOr(boolean x, boolean y) {
        return (x || y) && !(x && y);
    }
}
