package com.example.roommate.persistence.postgres;

import com.example.roommate.values.domainValues.BookedTimeframe;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Table("booked_timeframe")
public record BookedTimeframeDTO(@Id UUID id, DayOfWeek dayOfWeek, LocalTime localTime, Duration duration, UUID roomId) {
    public BookedTimeframe toBookedTimeFrame(){
        return new BookedTimeframe(dayOfWeek(), localTime(),duration());
    }
}
