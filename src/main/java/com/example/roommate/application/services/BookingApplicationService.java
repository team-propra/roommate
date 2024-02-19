package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.controller.HomeController;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.forms.KeyMasterForm;
import com.example.roommate.values.models.RoomBookingModel;
import com.example.roommate.values.models.RoomHomeModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static List<String> getItemsOfWorkspace(IWorkspace workspace) {
        return workspace.getItems().stream().map(ItemName::type).collect(Collectors.toList());
    }

    public static List<ItemName> convertToItemNameList(List<String> gegenstaende) {
        return gegenstaende.stream()
                .map(ItemName::new)
                .toList();
    }

    @PostConstruct
    public void initialize() {
        roomDomainService.addDummyDummy();
    }
    public void addBookEntry(IntermediateBookDataForm form, String userHandle) throws NotFoundException, GeneralDomainException {
        if(form == null) throw new IllegalArgumentException();
        UUID workspaceId = form.bookDataForm().workspaceId();
        UUID roomId = form.bookDataForm().roomId();

        List<BookedTimeframe> bookedTimeframes = IterableSupport.toList(form.bookingDays().toBookedTimeframes(userHandle));

        try {
            for (BookedTimeframe bookedTimeframe : bookedTimeframes) {
                roomDomainService.addBooking(bookedTimeframe, workspaceId, roomId);
            }
            if (bookedTimeframes.isEmpty())
                throw new GeneralDomainException();
        } catch (NotFoundRepositoryException e) {

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
        if (!IterableSupport.toList(room.getWorkspaces()).isEmpty())
            for (IWorkspace workspace : room.getWorkspaces()) {
                try {
                    roomDomainService.addWorkspace(room, workspace);
                } catch (NotFoundRepositoryException e) {
                    throw new NotFoundException();
                }
            }
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundException {
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
                        .anyMatch(workspace -> RoomDomainService.isWorkspaceAvailable(workspace, bookedTimeframe))
                )
                .toList();
        List<RoomBookingModel> availableWorkspaces = availableRooms.stream()
                .flatMap(room -> IterableSupport.toList(room.getWorkspaces()).stream()
                        .map(workspace ->
                                new RoomBookingModel(room.getRoomID(), workspace.getId(), workspace.getWorkspaceNumber(), room.getRoomNumber().number(), workspace.getItems())
                        )
                ).toList();
        return availableWorkspaces.stream()
                .filter(rbm -> new HashSet<>(IterableSupport.toList(rbm.itemNameList())).containsAll(items))
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
                    List<? extends IWorkspace> workspaces = IterableSupport.toList(room.getWorkspaces());
                    for(IWorkspace w: workspaces) {
                        List<BookedTimeframe> bookedTimeframes = IterableSupport.toList(w.getBookedTimeframes());
                        for(BookedTimeframe bookedTimeframe : bookedTimeframes)
                            if(bookedTimeframe.userHandle().equals(handle)) {
                                result.add(new KeyMasterForm(w.getId(), keyId));
                            }
                    }
                }
            }
        }
        return result;
    }

    public void removeItem(String itemName) {
        roomDomainService.removeItem(itemName);
    }

    public List<IWorkspace> getAllWorkspaces(Collection<IRoom> roomList) {
        return roomList.stream()
                .flatMap(room -> StreamSupport.stream(room.getWorkspaces().spliterator(), false))
                .collect(Collectors.toList());
    }

    public IWorkspace getWorkspace(IRoom room, UUID workspaceId) throws NotFoundException {
        Optional<? extends IWorkspace> optionalWorkspace = IterableSupport.toList(room.getWorkspaces()).stream()
                                                            .filter(x -> x.getId().equals(workspaceId))
                                                            .findFirst();
        if (optionalWorkspace.isEmpty())
            throw new NotFoundException();

        IWorkspace workspace = optionalWorkspace.get();
        return workspace;
    }

    public List<String> getUnusedItems(List<String> UsedItemsOfWorkspace) {
        return allItems()
                .stream()
                .map(ItemName::type)
                .filter(type -> !UsedItemsOfWorkspace.contains(type))
                .toList();
    }

    public List<RoomHomeModel> getRoomHomeModels() {
        return getRooms().stream()
                .flatMap(BookingApplicationService::toRoomHomeModel)
                .toList();
    }
    private static Stream<RoomHomeModel> toRoomHomeModel(IRoom room) {
        List<RoomHomeModel> list = IterableSupport.toList(room.getWorkspaces()).stream()
                .filter(workspace -> !IterableSupport.toList(workspace.getBookedTimeframes()).isEmpty())
                .map(workspace -> new RoomHomeModel(room.getRoomID(),
                        workspace.getId(),
                        room.getRoomNumber(),
                        workspace.getWorkspaceNumber(),
                        DayTimeFrame.from(IterableSupport.toList(workspace.getBookedTimeframes())).convertToString(),
                        workspace.getItems()
                ))
                .toList();
        return list.stream();
    }



    public void addWorkspace(String workspaceString, UUID roomID) throws NotFoundRepositoryException {
        int workspaceNumber;
        try {
            workspaceNumber = Integer.parseInt(workspaceString);
        } catch (NumberFormatException e) {
            workspaceNumber = 42;
        }

        roomDomainService.addWorkspace(workspaceNumber, roomID);
    }

    public void removeWorkspace(UUID workspaceID, UUID roomID) throws NotFoundRepositoryException {
        roomDomainService.removeWorkspace(workspaceID, roomID);
    }
}
