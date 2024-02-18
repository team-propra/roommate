package com.example.roommate.persistence.ephemeral;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("test")
public class RoomRepository implements IRoomRepository {
    private final List<RoomEntry> rooms;
    
    public RoomRepository(Collection<IRoom> rooms){
        //statically rendered
        this.rooms = rooms.stream()
                .map(RoomEntry::new)
                .toList();
    }
    
    //only temporary to not interrupt the application flow
    public RoomRepository(){
        rooms = new ArrayList<>();
    }
    
    public void overwrite(RoomEntry roomEntry) {
        for(int i=0;i<rooms.size();++i){
            if(rooms.get(i).getRoomID().equals(roomEntry.getRoomID())){
                rooms.set(i,roomEntry);
                return;
            }
        }

    }

    public List<? extends IRoom> findAll(){
        return rooms.stream().toList();
    }

    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<RoomEntry> first = rooms.stream().filter(r -> r.getRoomID().equals(roomID)).findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    public void remove(UUID room){
        rooms.stream().filter(r -> r.getRoomID().equals(room)).findFirst().ifPresent(rooms::remove);
    }

    public void add(IRoom room){
        rooms.add(new RoomEntry(room));
    }

    @Override
    public void addBooking(BookedTimeframe bookedTimeframe, IWorkspace workspace) throws NotFoundRepositoryException {
        IRoom room = findRoomByWorkspace(workspace.getId());
        List<BookedTimeframe> bookedTimeframes = new ArrayList<>();
        bookedTimeframes.add(bookedTimeframe);
        bookedTimeframes.addAll(IterableSupport.toList(workspace.getBookedTimeframes()));

        List<IWorkspace> filteredWorkspaces = new ArrayList<>();
        IterableSupport.toList(room.getWorkspaces())
                .stream()
                //.filter(workspaceEntry->workspaceEntry.getId().equals(workspace.getId()))
                .filter(workspaceEntry->!workspaceEntry.getId().equals(workspace.getId()))
                .forEach(filteredWorkspaces::add);
        filteredWorkspaces.add(new WorkspaceEntry(workspace.getId(),workspace.getWorkspaceNumber(),workspace.getItems(),bookedTimeframes));

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),filteredWorkspaces);
        overwrite(roomEntry);
    }

    @Override
    public void addItem(ItemName itemName, IWorkspace workspace) throws NotFoundRepositoryException {
        IRoom room = findRoomByWorkspace(workspace.getId());

        List<ItemName> itemNames = new ArrayList<>();
        IterableSupport.toList(workspace.getItems()).stream()
                .filter(x-> !x.type().equals(itemName.type()))
                .forEach(itemNames::add);
        itemNames.add(itemName);

        List<IWorkspace> filteredWorkspaces = new ArrayList<>();
        IterableSupport.toList(room.getWorkspaces())
                .stream()
                .filter(workspaceEntry->workspaceEntry.getId().equals(workspace.getId()))
                .forEach(filteredWorkspaces::add);
        filteredWorkspaces.add(new WorkspaceEntry(workspace.getId(),workspace.getWorkspaceNumber(),itemNames, workspace.getBookedTimeframes()));
        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),filteredWorkspaces);
        overwrite(roomEntry);
    }
    
    
    private IRoom findRoomByWorkspace(UUID workspaceId) throws NotFoundRepositoryException {
        Optional<RoomEntry> first = rooms.stream()
                .filter(roomEntry -> {
                    List<? extends IWorkspace> list = IterableSupport.toList(roomEntry.workspaces()).stream()
                            .filter(workspaceEntry -> workspaceEntry.getId() == workspaceId)
                            .toList();
                    return list.size() == 1;
                })
                .findFirst();
        if(first.isEmpty())
            throw new NotFoundRepositoryException();
        return first.get();
    }

    @Override
    public void removeItem(ItemName itemName, IWorkspace workspace) throws NotFoundRepositoryException {
        IRoom room = findRoomByWorkspace(workspace.getId());

        

        List<ItemName> oldItemsWithoutItemName = IterableSupport.toList(workspace.getItems()).stream().filter(x-> !x.type().equals(itemName.type())).toList();
        List<ItemName> itemNames = new ArrayList<>(oldItemsWithoutItemName);
        
        List<IWorkspace> filteredWorkspaces = new ArrayList<>();
        IterableSupport.toList(room.getWorkspaces())
                .stream()
                .filter(workspaceEntry->workspaceEntry.getId() != workspaceEntry.getId())
                .forEach(filteredWorkspaces::add);
        filteredWorkspaces.add(new WorkspaceEntry(workspace.getId(),workspace.getWorkspaceNumber(),itemNames, workspace.getBookedTimeframes()));
        
        RoomEntry roomEntry = new RoomEntry(room.getRoomID(),room.getRoomNumber(),filteredWorkspaces);
        overwrite(roomEntry);
    }

    @Override
    public void addWorkspace(IRoom room, IWorkspace workspace) throws NotFoundRepositoryException {
        _consistentAddWorkspace(workspace,room.getRoomID());
    }

    @Override
    public void removeWorkspace(UUID workspaceID, IRoom roomByID) {
        //TODO
    }

    private void _consistentAddWorkspace(IWorkspace workspace, UUID roomId) throws NotFoundRepositoryException {
        IRoom actuallyStoredRoom = findRoomByID(roomId);

        List<IWorkspace> filteredWorkspaces = new ArrayList<>();
        IterableSupport.toList(actuallyStoredRoom.getWorkspaces())
                .stream()
                .filter(workspaceEntry -> workspaceEntry.getId() != workspaceEntry.getId())//always false?
                .forEach(filteredWorkspaces::add);
        filteredWorkspaces.add(new WorkspaceEntry(workspace.getId(), workspace.getWorkspaceNumber(), workspace.getItems(), workspace.getBookedTimeframes()));
        overwrite(new RoomEntry(actuallyStoredRoom.getRoomID(), actuallyStoredRoom.getRoomNumber(), filteredWorkspaces));
    }

}
