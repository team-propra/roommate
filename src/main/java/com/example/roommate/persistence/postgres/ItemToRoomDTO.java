package com.example.roommate.persistence.postgres;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("item_to_room")
public record ItemToRoomDTO(@Id UUID id, String itemName, UUID roomID) {
}
