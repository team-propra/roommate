package com.example.roommate.stubs;

import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.domain.services.IRoomDomainService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RoomDomainServiceDummy implements IRoomDomainService {
    @Override
    public void addBooking(BookedTimeframe bookedTimeframe, UUID roomID) throws NotFoundRepositoryException {
        
    }

    @Override
    public void addRoom(RoomApplicationData roomApplicationData) {

    }

    @Override
    public void removeRoom(RoomApplicationData room) {

    }

    @Override
    public Collection<IRoom> getRooms() {
        return null;
    }

    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        return null;
    }

    @Override
    public List<ItemName> getItems() {
        return null;
    }

    @Override
    public void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {

    }

    @Override
    public void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {

    }

    @Override
    public void createItem(String itemName) {

    }
}
