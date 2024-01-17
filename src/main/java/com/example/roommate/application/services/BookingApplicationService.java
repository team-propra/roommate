package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.BookingApplicationData;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IRoom;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationService
public class BookingApplicationService {
    
    BookEntryDomainService bookEntryDomainService;
    RoomDomainService roomDomainService;

    public BookingApplicationService(BookEntryDomainService bookEntryDomainService, RoomDomainService roomDomainService) {
        this.bookEntryDomainService = bookEntryDomainService;
        this.roomDomainService = roomDomainService;
        roomDomainService.addDummieRooms();
    }

    public Collection<IBooking> getBookEntries() {
        return bookEntryDomainService.getBookEntries();
    }


    public void addBookEntry(IntermediateBookDataForm form) throws Exception {
        if(form == null) throw new IllegalArgumentException();
        bookEntryDomainService.addBocking(new BookingApplicationData(UUID.fromString(form.bookDataForm().roomID()), form.bookingDays()));
        UUID uuid = UUID.fromString(form.bookDataForm().roomID());
        roomDomainService.addBooking(form, uuid);
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
}
