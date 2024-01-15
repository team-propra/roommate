package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.NotFoundRepositoryException;

import java.util.List;
import java.util.UUID;


@RepositoryInterface
public interface IRoomRepository {
    List<IRoom> findAll();
    IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException;
    void remove(UUID room);
    void add(IRoom room);
    void saveAll(List<IRoom> rooms);
}
