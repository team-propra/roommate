package com.example.roommate.values.domainValues;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public record BookedTimeframe(DayOfWeek day, LocalTime startTime, Duration duration) {
}
