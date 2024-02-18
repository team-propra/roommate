package com.example.roommate.domain.services;

import com.example.roommate.annotations.DomainService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.values.domainValues.RoomNumber;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DomainService
//mediate between Repository, domain; map forms to domain-objects/data
@SuppressFBWarnings(value="EI2", justification="Repositories are properly injected, @Lazy self is required for transactions")
public class RoomDomainService {

    IRoomRepository roomRepository;
    IItemRepository itemRepository;
    RoomDomainService self;

    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
        this.self = this;
    }
    @Autowired
    public RoomDomainService(IRoomRepository roomRepository, IItemRepository itemRepository, @Lazy RoomDomainService self) {
        this.roomRepository = roomRepository;
        this.itemRepository = itemRepository;
        this.self = self;
    }

    public void addDummyDummy(){
        self.addDummieRooms();
    }

    @Transactional
    public void addDummieRooms() {
        ItemName chair = new ItemName("Chair");
        ItemName table = new ItemName("Table");
        ItemName desk = new ItemName("Desk");

        itemRepository.addItem(chair);
        itemRepository.addItem(table);
        itemRepository.addItem(desk);
        List<Workspace> room1Workspaces = List.of(
                new Workspace(UUID.randomUUID(), 1, List.of(table, desk, chair),List.of()),
                new Workspace(UUID.randomUUID(), 2, List.of(table),List.of()),
                new Workspace(UUID.randomUUID(), 3, List.of(desk),List.of())
        );

        List<Workspace> room2Workspaces = List.of(
                new Workspace(UUID.randomUUID(), 1, List.of(table),List.of()),
                new Workspace(UUID.randomUUID(), 44, List.of(chair, desk),List.of())
        );
        Room room2 = new Room(UUID.fromString("309d495f-036c-4b01-ab7e-8da2662bc75e"), new RoomNumber("13"),room1Workspaces);
        Room room1 = new Room(UUID.fromString("4d666ac8-efff-40a9-80a5-df9b82439f5a"), new RoomNumber("12"),room2Workspaces);
        roomRepository.add(room1);
        roomRepository.add(room2);
    }


    public void addBooking(BookedTimeframe bookedTimeframe, UUID workspaceId, UUID roomID) throws NotFoundRepositoryException {
        IRoom roomByID = roomRepository.findRoomByID(roomID);
        Optional<? extends IWorkspace> first = IterableSupport.toList(roomByID.getWorkspaces()).stream()
                .filter(x -> x.getId().equals(workspaceId))
                .findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        roomRepository.addBooking(bookedTimeframe,first.get());
    }


    public void addRoom(RoomApplicationData roomApplicationData){
        roomRepository.add(new Room(roomApplicationData.roomID(), roomApplicationData.roomNumber()));
    }
    

    public void removeRoom(RoomApplicationData room) {roomRepository.remove(room.roomID());}

    public Collection<IRoom> getRooms() {
        return roomRepository.findAll().stream()
                .map(room -> (IRoom) new Room(room.getRoomID(), room.getRoomNumber(),IterableSupport.toList(room.getWorkspaces())))
                .toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        try {
            return roomRepository.findRoomByID(roomID);
        } catch (NotFoundRepositoryException e) {
            throw new NotFoundRepositoryException();
        }
    }


    public List<ItemName> getItems() {
        return itemRepository.getItems();
    }

    public static boolean isWorkspaceAvailable(IWorkspace workspace, BookedTimeframe bookedTimeframe){
        return toWorkspace(workspace).isAvailable(bookedTimeframe);
    }

    private static Room toRoom(IRoom room){
        List<? extends IWorkspace> workspaces = IterableSupport.toList(room.getWorkspaces()).stream().toList();
        return new Room(
                room.getRoomID(),
                room.getRoomNumber(),
                IterableSupport.toList(workspaces)
        );
    }

    private static Workspace toWorkspace(IWorkspace workspace){
        return new Workspace(workspace.getId(),workspace.getWorkspaceNumber(),workspace.getItems(),workspace.getBookedTimeframes());
    }

    @Transactional
    public void _removeItemFromWorkspace(UUID workspaceId, String itemName, UUID roomId) throws NotFoundRepositoryException {
        Workspace workspace = getWorkspace(workspaceId, roomId);
        ItemName item = new ItemName(itemName);
        workspace.removeItem(item);
        roomRepository.removeItem(item, workspace);
    }

    private Workspace getWorkspace(UUID workspaceId, UUID roomId) throws NotFoundRepositoryException {
        IRoom iRoom = roomRepository.findRoomByID(roomId);
        List<Workspace> list = IterableSupport.toList(iRoom.getWorkspaces()).stream()
                .filter(w -> w.getId().equals(workspaceId))
                .map(RoomDomainService::toWorkspace)
                .toList();

        if(list.size() != 1) {
            throw new NotFoundRepositoryException();
        }
        return list.get(0);
    }

    public void removeItemFromWorkspace(UUID workspaceId, String itemName, UUID roomId) throws NotFoundRepositoryException {
        self._removeItemFromWorkspace(workspaceId, itemName, roomId);
    }

    @Transactional
    public void _addItemToWorkspace(UUID workspaceId, String itemName, UUID roomId) throws NotFoundRepositoryException {
        Workspace workspace = getWorkspace(workspaceId, roomId);

        ItemName item = new ItemName(itemName);
        workspace.addItem(item);
        roomRepository.addItem(item, workspace);
    }

    public void addItemToWorkspace(UUID workspaceId, String itemName, UUID roomId) throws NotFoundRepositoryException {
        self._addItemToWorkspace(workspaceId, itemName, roomId);
    }

    @Transactional
    public void _createItem(String itemName) {
        ItemName item = new ItemName(itemName);
        itemRepository.addItem(item);

    }

    public void createItem(String itemName) {
        self._createItem(itemName);
    }

    public void addWorkspace(IRoom room, IWorkspace x) throws NotFoundRepositoryException {
        roomRepository.addWorkspace(room, x);
    }

    public void removeItem(String itemName) {
        ItemName item = new ItemName(itemName);
        itemRepository.removeItem(item);
    }
}
