package com.example.roommate.persistence.postgres;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ItemToRoomDAO extends CrudRepository<ItemToRoomDTO, UUID> {

    List<ItemToRoomDTO> findByRoomID(UUID roomID);
}
