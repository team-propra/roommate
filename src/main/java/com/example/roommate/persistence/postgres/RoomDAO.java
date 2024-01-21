package com.example.roommate.persistence.postgres;


import org.springframework.data.repository.CrudRepository;

public interface RoomDAO extends CrudRepository<RoomDTO, Long> {
}
