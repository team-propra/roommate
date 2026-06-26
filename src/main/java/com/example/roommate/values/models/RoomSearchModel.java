package com.example.roommate.values.models;

import java.util.List;

public record RoomSearchModel(
        String date,
        String startTime,
        String endTime,
        List<ItemModel> items,
        List<String> selectedItems,
        List<RoomBookingModel> roomBookingModels
) {
}
