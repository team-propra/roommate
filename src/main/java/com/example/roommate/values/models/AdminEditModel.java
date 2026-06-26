package com.example.roommate.values.models;

import java.util.List;

public record AdminEditModel(List<ItemModel> itemList, List<AdminRoomModel> roomList) {
    public AdminEditModel {
        itemList = List.copyOf(itemList);
        roomList = List.copyOf(roomList);
    }
}
