package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationService
public class BookingApplicationService {
    
    RoomDomainService roomDomainService;

    public BookingApplicationService(RoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;
        roomDomainService.addDummieRooms();
    }

    public void addBookEntry(IntermediateBookDataForm form) throws NotFoundException, GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        UUID roomID = UUID.fromString(form.bookDataForm().roomID());

        List<BookedTimeframe> bookedTimeframes = form.bookingDays().toBookedTimeframes();
        try{
            for (BookedTimeframe bookedTimeframe : bookedTimeframes) {
                roomDomainService.addBooking(bookedTimeframe,roomID);
            }
            if(bookedTimeframes.isEmpty())
                throw new GeneralDomainException();
        }
        catch (NotFoundRepositoryException e){
            throw new NotFoundException();
        }
    }

    public List<IRoom> findRoomsWithItems(List<ItemName> items) {
            return roomDomainService.getRooms().stream()
                    .filter(room -> new HashSet<>(room.getItemNames()).containsAll(items))
                    .collect(Collectors.toList());
}

    public Collection<ItemName> getItems() {
        return roomDomainService.getItems();
    }

    public Collection<IRoom> getRooms() {
        return roomDomainService.getRooms();
    }

    public void addRoom(IRoom room) {
        roomDomainService.addRoom(new RoomApplicationData(room.getRoomID(), room.getRoomNumber()));
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundException{
        try {
            return roomDomainService.findRoomByID(roomID);
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundException();
        }
    }

    public void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        roomDomainService.removeItemFromRoom(roomID, itemName);
    }

    public void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        roomDomainService.addItemToRoom(roomID, itemName);
    }
}
