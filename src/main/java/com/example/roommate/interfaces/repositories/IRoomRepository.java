package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IWorkspace;
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
    void addBooking(BookedTimeframe bookedTimeframe, IWorkspace  workspace) throws NotFoundRepositoryException;

    void addItem(ItemName itemName, IWorkspace iWorkspace) throws NotFoundRepositoryException;

    void removeItem(ItemName itemName, IWorkspace iWorkspace) throws NotFoundRepositoryException;

    void addWorkspace(IRoom room, IWorkspace x) throws NotFoundRepositoryException;

    void removeWorkspace(UUID workspaceID, IRoom roomByID);
}
