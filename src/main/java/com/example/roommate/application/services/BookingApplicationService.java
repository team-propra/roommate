package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.BookingApplicationData;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.BookEntryDomainService;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IBooking;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IRoom;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
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


    public void addBookEntry(IntermediateBookDataForm form) throws GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        bookEntryDomainService.addBocking(new BookingApplicationData(UUID.fromString(form.bookDataForm().roomID()), form.bookingDays()));
    }

    public Collection<ItemName> getItems() {
        return roomDomainService.getItems();
    }

    public Collection<IRoom> getRooms() {
        return roomDomainService.getRooms();
    }

    public void addRoom(IRoom room) {
        roomDomainService.addRoom(room);
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundException{
        try {
            return roomDomainService.findRoomByID(roomID);
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundException();
        }
    }

    public List<IRoom> findRoomsWith(List<ItemName> items, String datum, String startUhrzeit, String endUhrzeit) {
        String weekday = getWeekday(datum);
        return roomDomainService.getRooms().stream()
                .filter(room -> room.getItemNames().containsAll(items))
                .filter(room -> room.isAvailable(weekday, startUhrzeit, endUhrzeit)) // an Datum ist Zeitraum frei
                .collect(Collectors.toList());
    }

    public static String getWeekday(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            //Convert DayOfWeek enum to a string representation and make it lower case (e.g. "monday")
            String weekday = dayOfWeek.toString().toLowerCase();
            return weekday;
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }
}
