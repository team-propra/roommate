package com.example.roommate.persistence.postgres;

import com.example.roommate.values.domainValues.BookedTimeframe;
import org.springframework.data.annotation.Id;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

public record BookedTimeframeDTO(@Id UUID uuid, DayOfWeek day, LocalTime startTime, Duration duration, UUID roomID) {
    public BookedTimeframe toBookedTimeFrame(){
        return new BookedTimeframe(day(),startTime(),duration());
    }
}
