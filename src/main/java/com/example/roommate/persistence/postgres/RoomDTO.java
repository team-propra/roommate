package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record RoomDTO(UUID roomID, String roomNumber) {

    //@GenericGenerator(name = "uuid2", strategy = "uuid2")
}
