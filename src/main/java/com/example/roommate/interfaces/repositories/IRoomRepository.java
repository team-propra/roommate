package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.List;
import java.util.UUID;


@RepositoryInterface
public interface IRoomRepository {
    List<? extends IRoom> findAll();
    IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException;
    void remove(UUID room);
    void add(IRoom room);
    void addBooking(BookedTimeframe bookedTimeframe, IRoom  room) throws NotFoundRepositoryException;

    void addItem(ItemName itemName, IRoom iRoom) throws NotFoundRepositoryException;

    void removeItem(ItemName itemName, IRoom iRoom) throws NotFoundRepositoryException;
}
