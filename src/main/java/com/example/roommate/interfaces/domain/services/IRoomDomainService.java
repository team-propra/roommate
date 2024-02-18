package com.example.roommate.interfaces.domain.services;

import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IRoomDomainService {
    void addBooking(BookedTimeframe bookedTimeframe, UUID roomID) throws NotFoundRepositoryException;
    void addRoom(RoomApplicationData roomApplicationData);
    void removeRoom(RoomApplicationData room);
    Collection<IRoom> getRooms();
    IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException;
    List<ItemName> getItems();
    void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException;
    void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException;
    void createItem(String itemName);
}
