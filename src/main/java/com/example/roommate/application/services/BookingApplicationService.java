package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IWorkspace;
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

    public Collection<ItemName> allItems() {
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
    
    public List<String> getItemsOfRoom(UUID roomId) throws NotFoundException {
        List<String> items = new ArrayList<>();
        IRoom room = findRoomByID(roomId);
        for (IWorkspace workspace : room.getWorkspaces()) {
            workspace.getItems().stream()
                    .map(ItemName::type)
                    .forEach(items::add);
        }
        return items;
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
                .filter(room -> RoomDomainService.isRoomAvailable(room, bookedTimeframe))
                .flatMap(room -> IterableSupport.toList(room.getWorkspaces()).stream()
                    .map(workspace->
                        new RoomBookingModel(room.getRoomID(),workspace.getId(),room.getRoomNumber().number(),workspace.getItems())
                    )
                )
                .filter(rbm-> new HashSet<>(IterableSupport.toList(rbm.itemNameList())).containsAll(items))
                .toList();
    }

    public void removeItemFromRoom(UUID workspaceID, String itemName, UUID roomID) throws NotFoundRepositoryException {
        roomDomainService.removeItemFromWorkspace(workspaceID, itemName, roomID);
    }

    public void addItemToRoom(UUID workspaceID, String itemName, UUID roomID) throws NotFoundRepositoryException {
        roomDomainService.addItemToWorkspace(workspaceID, itemName, roomID);
    }

    public void createItem(String itemName) {
        roomDomainService.createItem(itemName);
    }
}
