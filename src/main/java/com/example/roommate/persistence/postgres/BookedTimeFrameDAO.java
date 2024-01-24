package com.example.roommate.persistence.postgres;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BookedTimeFrameDAO extends CrudRepository<BookedTimeframeDTO, UUID> {
    List<BookedTimeframeDTO> findByRoomID(UUID roomID);
}
