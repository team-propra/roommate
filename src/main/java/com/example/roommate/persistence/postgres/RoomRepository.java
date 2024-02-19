package com.example.roommate.persistence.postgres;

import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.domainValues.RoomNumber;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("!test")
@SuppressFBWarnings(value="EI2", justification="DAO injection required for database interaction")
public class RoomRepository implements IRoomRepository {
    IRoomDAO roomDAO;

    IItemDAO itemDAO;

    IBookedTimeFrameDAO bookedTimeFrameDAO;

    IItemToWorkspaceDAO itemToWorkspaceDAO;

    IWorkspaceDAO workspaceDAO;

    public RoomRepository(IRoomDAO roomDAO, IItemDAO itemDAO, IBookedTimeFrameDAO bookedTimeFrameDAO, IItemToWorkspaceDAO itemToWorkspaceDAO, IWorkspaceDAO workspaceDAO) {
        this.roomDAO = roomDAO;
        this.itemDAO = itemDAO;
        this.bookedTimeFrameDAO = bookedTimeFrameDAO;
        this.itemToWorkspaceDAO = itemToWorkspaceDAO;
        this.workspaceDAO = workspaceDAO;
    }

    @Override
    public List<? extends IRoom> findAll() {
        // Query
        Iterable<RoomDTO> roomList = roomDAO.findAll();
        List<ItemToWorkspaceDTO> itemToWorkspaceList = IterableSupport.toList(itemToWorkspaceDAO.findAll());
        List<WorkspacesDTO> workspacesList = IterableSupport.toList(workspaceDAO.findAll());
        List<ItemDTO> itemList = IterableSupport.toList(itemDAO.findAll());
        List<BookedTimeframeDTO> bookedTimeframeList = IterableSupport.toList(bookedTimeFrameDAO.findAll());

        // Map
        List<RoomOOP> result = new ArrayList<>();
        for (RoomDTO room : roomList) {
            /*
            List<ItemToRoomDTO> matchingMaps = itemToWorkspaceList.stream()
                    .filter(itemMapEntry -> itemMapEntry.roomId().equals(room.id()))
                    .toList();

             */
            

            List<WorkspacesDTO> matchingWorkspaces = workspacesList.stream()
                    .filter(workspace -> workspace.roomId().equals(room.id()))
                    .toList();


            List<WorkspaceOOP> workspaces = matchingWorkspaces.stream().map(x->toWorkspace(x,itemToWorkspaceList,itemList,bookedTimeframeList)).toList();

            result.add(new RoomOOP(room.id(), new RoomNumber(room.roomNumber()), workspaces));
        }
        return result;
    }

    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<RoomDTO> room = roomDAO.findById(roomID);
        if (room.isEmpty())
            throw new NotFoundRepositoryException();
        List<BookedTimeframeDTO> timeframes = IterableSupport.toList(bookedTimeFrameDAO.findAll()).stream()
                .toList();
        List<WorkspacesDTO> workspacesDTOS = IterableSupport.toList(workspaceDAO.findAll()).stream()
                .filter(x -> x.roomId().equals(roomID))
                .toList();
        List<ItemToWorkspaceDTO> itemToWorkspaceDTOS = IterableSupport.toList(itemToWorkspaceDAO.findAll());
        List<ItemDTO> itemDTOS = IterableSupport.toList(itemDAO.findAll());
        List<WorkspaceOOP> workspaces = workspacesDTOS.stream().map(x->toWorkspace(x,itemToWorkspaceDTOS,itemDTOS,timeframes)).toList(); 
        return new RoomOOP(roomID, new RoomNumber(room.get().roomNumber()), workspaces);
    }
    
    private WorkspaceOOP toWorkspace(WorkspacesDTO workspacesDTO, List<ItemToWorkspaceDTO> itemToWorkspaceDTOS, List<ItemDTO> items, List<BookedTimeframeDTO> bookedTimeframeDTOS){
        List<ItemName> myItems = new ArrayList<>();
        List<BookedTimeframe> bookedTimeframes = new ArrayList<>(); 
        
        itemToWorkspaceDTOS.stream()
                .filter(x -> x.workspaceId().equals(workspacesDTO.id()))
                .forEach(workspace->{
                    items.stream()
                        .map(ItemDTO::toItemName)
                        .filter(item-> item.type().equals(workspace.itemName()))
                        .forEach(myItems::add);
                });

        bookedTimeframeDTOS.stream()
                .filter(timeframe -> timeframe.workspaceId().equals(workspacesDTO.id()))
                .map(BookedTimeframeDTO::toBookedTimeFrame)
                .forEach(bookedTimeframes::add);
        
        return new WorkspaceOOP(workspacesDTO.id(),workspacesDTO.workspaceNumber(), myItems,bookedTimeframes);
    }

    @Override
    public void remove(UUID roomID) {
        roomDAO.deleteById(roomID);
    }

    @Override
    public void add(IRoom room) {
        System.out.println(room.getRoomID());
        roomDAO.insert(room.getRoomID(), room.getRoomNumber().number());
        room.getWorkspaces()
                .forEach(workspace->{
                    workspaceDAO.insert(workspace.getId(),workspace.getWorkspaceNumber(),room.getRoomID());
                    workspace.getItems().forEach(item-> itemToWorkspaceDAO.insert(UUID.randomUUID(),item.type(),workspace.getId()));
                    workspace.getBookedTimeframes().forEach(bookedTimeframe -> {
                        bookedTimeFrameDAO.insert(UUID.randomUUID(),bookedTimeframe.day(),bookedTimeframe.startTime(),bookedTimeframe.duration(),room.getRoomID(), bookedTimeframe.userHandle());
                    });
                });
    }

    @Override
    public void addBooking(BookedTimeframe bookedTimeframe, IWorkspace workspace) {
        bookedTimeFrameDAO.insert(UUID.randomUUID(), bookedTimeframe.day(), bookedTimeframe.startTime(), bookedTimeframe.duration(), workspace.getId(), bookedTimeframe.userHandle());
    }

    @Override
    public void addItem(ItemName itemName, IWorkspace iWorkspace) {
        itemToWorkspaceDAO.insert(UUID.randomUUID(),itemName.toString(),iWorkspace.getId());
    }

    @Override
    public void removeItem(ItemName itemName, IWorkspace iWorkspace) throws NotFoundRepositoryException {
        List<ItemToWorkspaceDTO> filtered = itemToWorkspaceDAO.findByWorkspaceId(iWorkspace.getId()).stream()
                .filter(x-> x.itemName().equals(itemName.type()))
                .toList();
        if(filtered.size() != 1)
            throw new NotFoundRepositoryException();
        itemToWorkspaceDAO.delete(filtered.get(0));
    }

    @Override
    public void addWorkspace(IRoom room, IWorkspace workspace) throws NotFoundRepositoryException {
        workspaceDAO.insert(workspace.getId(),workspace.getWorkspaceNumber(),room.getRoomID());
        workspace.getBookedTimeframes().forEach(bookedTimeframe -> addBooking(bookedTimeframe,workspace));
        workspace.getItems().forEach(item -> addItem(item,workspace));
    }

    @Override
    public void removeWorkspace(UUID workspaceID, IRoom roomByID) {
        // TODO
    }


}
