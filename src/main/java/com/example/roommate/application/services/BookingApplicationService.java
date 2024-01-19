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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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

    public List<IRoom> findRoomsWith(List<ItemName> items, String dateString, String startTimeString, String endTimeString) {
        LocalDate date = LocalDate.parse(dateString);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
        LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);



        return roomDomainService.getRooms().stream()
                .filter(room -> room.getItemNames().containsAll(items))
                //.filter(room -> room.isAvailable(weekday, startUhrzeit, endUhrzeit)) // an Datum ist Zeitraum frei
                .collect(Collectors.toList());
    }
}
