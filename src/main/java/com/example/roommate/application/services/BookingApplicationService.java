package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.models.RoomBookingModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.annotation.PostConstruct;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.DayOfWeek;
@ApplicationService
@SuppressFBWarnings(value="EI2", justification="RoomDomainService is properly injected")
public class BookingApplicationService {

    RoomDomainService roomDomainService;

    public BookingApplicationService(RoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;

    }

    @PostConstruct
    public void initialize(){
        roomDomainService.addDummyDummy();
    }
    public void addBookEntry(IntermediateBookDataForm form) throws NotFoundException, GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        UUID roomID = form.bookDataForm().id();

        List<BookedTimeframe> bookedTimeframes = IterableSupport.toList(form.bookingDays().toBookedTimeframes());
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

    public List<RoomBookingModel> findAvailableRoomsWithItems(List<ItemName> items, String dateString, String startTimeString, String endTimeString) {
        LocalDate date = LocalDate.parse(dateString);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
        LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);
        Duration duration = Duration.between(startTime, endTime);

        BookedTimeframe bookedTimeframe = new BookedTimeframe(dayOfWeek, startTime, duration);

        return roomDomainService.getRooms().stream()
                .filter(room -> new HashSet<>(IterableSupport.toList(room.getItemNames())).containsAll(items))
                .filter(room -> RoomDomainService.isRoomAvailable(room, bookedTimeframe))
                .map(x->new RoomBookingModel(x.getRoomID(),x.getRoomNumber().number(),x.getItemNames()))
                .collect(Collectors.toList());
    }

    public void removeItemFromRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        roomDomainService.removeItemFromWorkspace(roomID, itemName);
    }

    public void addItemToRoom(UUID roomID, String itemName) throws NotFoundRepositoryException {
        roomDomainService.addItemToWorkspace(roomID, itemName);
    }

    public void createItem(String itemName) {
        roomDomainService.createItem(itemName);
    }
}
