package com.example.roommate.application.data;

import com.example.roommate.annotations.ApplicationData;
import com.example.roommate.values.domainValues.RoomNumber;

import java.util.UUID;

@ApplicationData
public record RoomApplicationData(UUID roomID, RoomNumber roomNumber) {

}
