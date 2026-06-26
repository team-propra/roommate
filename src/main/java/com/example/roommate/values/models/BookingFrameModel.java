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
}
