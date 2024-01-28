package com.example.roommate.persistence.postgres;


import com.example.roommate.annotations.Interface;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

@Interface
public interface IRoomDAO extends CrudRepository<RoomDTO, UUID> {
//    RoomDTO findByRoomId(UUID roomId);
}
