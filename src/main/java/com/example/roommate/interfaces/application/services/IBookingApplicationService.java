package com.example.roommate.interfaces.application.services;

import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.models.RoomBookingModel;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IBookingApplicationService {
    void addBookEntry(IntermediateBookDataForm form) throws NotFoundException, GeneralDomainException;
    Collection<ItemName> getItems();
    Collection<IRoom> getRooms();
    void addRoom(IRoom room);
    IRoom findRoomByID(UUID roomID) throws NotFoundException;
    List<RoomBookingModel> findAvailableRoomsWithItems(List<ItemName> items, String dateString, String startTimeString, String endTimeString);
    void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException;
    void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException;
    void createItem(String itemName);
}
