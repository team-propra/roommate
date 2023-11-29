package com.example.roommate.interfaces;

import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;

import java.util.List;
import java.util.UUID;

public interface IRoomRepository {
    List<Room> findAll();
    Room findRoomByID(UUID roomID) throws NotFoundRepositoryException;
    void remove(Room room);
    void save(Room room);
    void saveAll(List<Room> rooms);
}
