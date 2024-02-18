package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.forms.KeyMasterForm;
import com.example.roommate.values.models.RoomBookingModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

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
    UserDomainService userDomainService;

    @Autowired
    public BookingApplicationService(RoomDomainService roomDomainService, UserDomainService userDomainService) {
        this.roomDomainService = roomDomainService;
        this.userDomainService = userDomainService;

    }

    @PostConstruct
    public void initialize(){
        roomDomainService.addDummyDummy();
    }
    public void addBookEntry(IntermediateBookDataForm form, String userHandle) throws NotFoundException, GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        UUID workspaceId = form.bookDataForm().workspaceId();
        UUID roomId = form.bookDataForm().roomId();

        List<BookedTimeframe> bookedTimeframes = IterableSupport.toList(form.bookingDays().toBookedTimeframes(userHandle));

        try{
            for (BookedTimeframe bookedTimeframe : bookedTimeframes) {
                roomDomainService.addBooking(bookedTimeframe,workspaceId,roomId);
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

    public void addRoom(IRoom room) throws NotFoundException {
        roomDomainService.addRoom(new RoomApplicationData(room.getRoomID(), room.getRoomNumber()));
        if(!IterableSupport.toList(room.getWorkspaces()).isEmpty())
            for (IWorkspace workspace : room.getWorkspaces()) {
                try {
                    roomDomainService.addWorkspace(room, workspace);
                } catch (NotFoundRepositoryException e) {
                    throw new NotFoundException();
                }
            }
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

    public List<RoomBookingModel> findAvailableWorkspacesWithItems(List<ItemName> items, String dateString, String startTimeString, String endTimeString, String userHandle) {
        LocalDate date = LocalDate.parse(dateString);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
        LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);
        Duration duration = Duration.between(startTime, endTime);

        BookedTimeframe bookedTimeframe = new BookedTimeframe(dayOfWeek, startTime, duration, userHandle);

        Collection<IRoom> rooms = roomDomainService.getRooms();
        List<IRoom> availableRooms = rooms.stream()
                .filter(room -> IterableSupport.toList(room.getWorkspaces()).stream()
                        .anyMatch(workspace->RoomDomainService.isWorkspaceAvailable(workspace, bookedTimeframe))
                )
                .toList();
        List<RoomBookingModel> availableWorkspaces = availableRooms.stream()
                .flatMap(room -> IterableSupport.toList(room.getWorkspaces()).stream()
                        .map(workspace ->
                                new RoomBookingModel(room.getRoomID(), workspace.getId(), workspace.getWorkspaceNumber(), room.getRoomNumber().number(), workspace.getItems())
                        )
                ).toList();
        return availableWorkspaces.stream()
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
    public Iterable<KeyMasterForm> getAssociatedBookEntries() {
        List<? extends IUser> users = userDomainService.getAllUser();
        Collection<IRoom> rooms = roomDomainService.getRooms();

        List<KeyMasterForm> result = new ArrayList<>();

        for(IUser user : users) {
            if(user.getRole().equals("VERIFIED_USER")) {
                UUID keyId = user.getKeyId();
                String handle = user.getHandle();
                for(IRoom room : rooms) {
                    List<Workspace> workspaces = (List<Workspace>) IterableSupport.toList(room.getWorkspaces());
                    for(Workspace w: workspaces) {
                        List<BookedTimeframe> bookedTimeframes = IterableSupport.toList(w.getBookedTimeframes());
                        for(BookedTimeframe bookedTimeframe : bookedTimeframes)
                            if(bookedTimeframe.userHandle().equals(handle)) {
                                result.add(new KeyMasterForm(w.getId(), keyId));
                            }
                    }
                }
            }
        }

        //ToDo Streams: Alle Räume, alle User durchgehen, und räume mit BookedTimeFrames.userHanlde.equals(user ....)
        return result;
    }
}
