package com.example.roommate.persistence.postgres;


import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@Interface
public interface IRoomDAO extends CrudRepository<RoomDTO, UUID> {
//    RoomDTO findByRoomId(UUID roomId);
    @Query("INSERT INTO room (id,room_number) VALUES (:id, :roomNumber)")
    @Modifying
    public void insert(@Param("id") UUID id, @Param("roomNumber") String roomNumber);
}
