package com.example.roommate.interfaces.repositories;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.exceptions.NotFoundRepositoryException;

import java.util.List;
import java.util.UUID;

public interface IRoomRepository {
    List<IRoom> findAll();
    IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException;
    void remove(UUID room);
    void save(IRoom room);
    void saveAll(List<IRoom> rooms);
}