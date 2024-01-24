package com.example.roommate.persistence.postgres;


import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoomDAO extends CrudRepository<RoomDTO, UUID> {
    RoomDTO findByRoomID(UUID roomID);
}
