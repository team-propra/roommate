package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@Interface
public interface IBookedTimeFrameDAO extends CrudRepository<BookedTimeframeDTO, UUID> {
    List<BookedTimeframeDTO> findByRoomId(UUID roomID);
}
