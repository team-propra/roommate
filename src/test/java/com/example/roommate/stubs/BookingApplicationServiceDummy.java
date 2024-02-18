package com.example.roommate.stubs;

import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.application.services.IBookingApplicationService;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.models.RoomBookingModel;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BookingApplicationServiceDummy implements IBookingApplicationService {
    @Override
    public void addBookEntry(IntermediateBookDataForm form) throws NotFoundException, GeneralDomainException {
        
    }

    @Override
    public Collection<ItemName> getItems() {
        return null;
    }

    @Override
    public Collection<IRoom> getRooms() {
        return null;
    }

    @Override
    public void addRoom(IRoom room) {

    }

    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundException {
        return null;
    }

    @Override
    public List<RoomBookingModel> findAvailableRoomsWithItems(List<ItemName> items, String dateString, String startTimeString, String endTimeString) {
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
