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
    public RoomSearchModel {
        items = List.copyOf(items);
        selectedItems = List.copyOf(selectedItems);
        roomBookingModels = List.copyOf(roomBookingModels);
    }
}
