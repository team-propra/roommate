package com.example.roommate.application.data;

import com.example.roommate.annotations.ApplicationData;

import java.util.UUID;

@ApplicationData
public record RoomApplicationData(UUID roomID, String roomNumber) {

}
