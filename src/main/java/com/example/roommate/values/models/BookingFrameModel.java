package com.example.roommate.values.models;

import java.util.List;

public record BookingFrameModel(
        int stepSize,
        int days,
        int times,
        List<String> dayLabels,
        List<String> timeLabels,
        List<List<Boolean>> reserved
) {
    public BookingFrameModel {
        dayLabels = List.copyOf(dayLabels);
        timeLabels = List.copyOf(timeLabels);
        reserved = reserved.stream()
                .map(List::copyOf)
                .toList();
    }

    @Override
    public List<List<Boolean>> reserved() {
        return reserved.stream()
                .map(List::copyOf)
                .toList();
    }
}
