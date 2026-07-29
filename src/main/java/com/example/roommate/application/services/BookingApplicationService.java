package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.exceptions.ArgumentValidationException;
import com.example.roommate.exceptions.domainService.GeneralDomainException;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookingDays;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.DayTimeFrame;
import com.example.roommate.values.domainValues.IntermediateBookDataForm;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.values.forms.KeyMasterForm;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.*;
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

    private static List<ItemModel> toItemModels(Collection<ItemName> items) {
        return items.stream()
                .map(item -> new ItemModel(item.type()))
                .toList();
    }

    private static BookingFrameModel toBookingFrameModel(DayTimeFrame frame) {
        return new BookingFrameModel(
                frame.stepSize(),
                frame.days(),
                frame.times(),
                frame.dayLabels(),
                frame.timeLabels(),
                frame.reserved()
        );
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

    public AdminEditModel getAdminEditModel() {
        List<AdminRoomModel> rooms = getRooms().stream()
                .map(room -> new AdminRoomModel(room.getRoomID(), room.getRoomNumber().number()))
                .toList();
        return new AdminEditModel(toItemModels(allItems()), rooms);
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

    public RoomOverviewModel getRoomOverviewModel(UUID roomID) throws NotFoundException {
        return getRoomOverviewModel(roomID, Locale.GERMAN);
    }

    public RoomOverviewModel getRoomOverviewModel(UUID roomID, Locale locale) throws NotFoundException {
        IRoom room = findRoomByID(roomID);
        List<WorkspaceOverviewModel> workspaces = IterableSupport.toList(room.getWorkspaces()).stream()
                .map(workspace -> new WorkspaceOverviewModel(
                        workspace.getId(),
                        workspace.getWorkspaceNumber(),
                        workspace.getItems().stream().map(ItemName::type).toList(),
                        IterableSupport.toList(workspace.getBookedTimeframes()).stream()
                                .map(bookedTimeframe -> DayTimeFrame.from(List.of(bookedTimeframe), locale).convertToString(locale))
                                .toList()
                ))
                .toList();
        return new RoomOverviewModel(room.getRoomID(), room.getRoomNumber().number(), workspaces);
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

    public RoomSearchModel getRoomSearchModel(List<String> selectedItems, SearchTimeForm timeForm, String userHandle) {
        List<String> normalizedSelectedItems = selectedItems == null ? List.of() : selectedItems;
        List<ItemName> selectedItemNames = convertToItemNameList(normalizedSelectedItems);
        List<RoomBookingModel> availableWorkspacesWithItems = findAvailableWorkspacesWithItems(
                selectedItemNames,
                timeForm.datum(),
                timeForm.startUhrzeit(),
                timeForm.endUhrzeit(),
                userHandle
        );

        return new RoomSearchModel(
                timeForm.datum(),
                timeForm.startUhrzeit(),
                timeForm.endUhrzeit(),
                toItemModels(allItems()),
                normalizedSelectedItems,
                availableWorkspacesWithItems
        );
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
    public List<KeyMasterForm> getAssociatedBookEntries() {
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

    public WorkspaceDetailsModel getWorkspaceDetailsModel(UUID roomId, UUID workspaceId) throws NotFoundException {
        return getWorkspaceDetailsModel(roomId, workspaceId, Locale.GERMAN);
    }

    public WorkspaceDetailsModel getWorkspaceDetailsModel(UUID roomId, UUID workspaceId, Locale locale) throws NotFoundException {
        IRoom room = findRoomByID(roomId);
        IWorkspace workspace = getWorkspace(room, workspaceId);
        List<String> itemsOfWorkspace = getItemsOfWorkspace(workspace);
        List<String> filteredItems = getUnusedItems(itemsOfWorkspace);
        DayTimeFrame dayTimeFrame = DayTimeFrame.from(workspace.getBookedTimeframes(), locale);

        return new WorkspaceDetailsModel(
                room.getRoomID(),
                room.getRoomNumber().number(),
                workspace.getId(),
                workspace.getWorkspaceNumber(),
                itemsOfWorkspace,
                filteredItems,
                toBookingFrameModel(dayTimeFrame)
        );
    }

    public List<String> getUnusedItems(List<String> UsedItemsOfWorkspace) {
        return allItems()
                .stream()
                .map(ItemName::type)
                .filter(type -> !UsedItemsOfWorkspace.contains(type))
                .toList();
    }

    public List<RoomHomeModel> getRoomHomeModels() {
        return getRoomHomeModels(Locale.GERMAN);
    }

    public List<RoomHomeModel> getRoomHomeModels(Locale locale) {
        return getRooms().stream()
                .flatMap(room -> BookingApplicationService.toRoomHomeModel(room, locale))
                .toList();
    }
    private static Stream<RoomHomeModel> toRoomHomeModel(IRoom room, Locale locale) {
        List<RoomHomeModel> list = IterableSupport.toList(room.getWorkspaces()).stream()
                .filter(workspace -> !IterableSupport.toList(workspace.getBookedTimeframes()).isEmpty())
                .map(workspace -> new RoomHomeModel(room.getRoomID(),
                        workspace.getId(),
                        room.getRoomNumber(),
                        workspace.getWorkspaceNumber(),
                        DayTimeFrame.from(IterableSupport.toList(workspace.getBookedTimeframes()), locale).convertToString(locale),
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

    public boolean isBookingSelectionValid(BookDataForm form, List<String> checkedDays) throws ArgumentValidationException {
        return BookingDays.validateBookingCoorectness(BookingDays.from(form.stepSize(), checkedDays));
    }

    public void addBookEntry(BookDataForm form, List<String> checkedDays, String userHandle) throws NotFoundException, GeneralDomainException, ArgumentValidationException {
        IntermediateBookDataForm addedBookingsForm = BookDataForm.addBookingsToForm(checkedDays, form);
        addBookEntry(addedBookingsForm, userHandle);
    }
}
