package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record ItemToRoomDTO(@Id UUID id, String itemName, UUID roomID) {
}
