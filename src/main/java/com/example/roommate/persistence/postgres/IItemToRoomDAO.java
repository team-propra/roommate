package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Interface
public interface IItemToRoomDAO extends CrudRepository<ItemToRoomDTO, UUID> {

    List<ItemToRoomDTO> findByRoomId(UUID roomID);

    @Query("INSERT INTO item_to_room (id, item_name, room_id) VALUES (:id, :itemName, :roomId)")
    @Modifying
    void insert(@Param("id")UUID id, @Param("itemName") String itemName, @Param("roomId") UUID roomId);

    @Query("DELETE FROM item_to_room WHERE item_name = :itemName AND room_id = :roomId")
    @Modifying
    void delete(@Param("itemName") String itemName, @Param("roomId") UUID roomId);

}
