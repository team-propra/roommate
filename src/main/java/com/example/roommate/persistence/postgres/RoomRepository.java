package com.example.roommate.persistence.postgres;

import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.exceptions.NotFoundRepositoryException;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.interfaces.entities.IWorkspace;
import com.example.roommate.interfaces.repositories.IRoomRepository;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("!test")
public class RoomRepository implements IRoomRepository {
    IRoomDAO roomDAO;

    IItemDAO itemDAO;

    IBookedTimeFrameDAO bookedTimeFrameDAO;

    IItemToRoomDAO itemToRoomDAO;

    IItemToWorkspaceDAO itemToWorkspaceDAO;

    IWorkspaceDAO workspaceDAO;

    public RoomRepository(IRoomDAO roomDAO, IItemDAO itemDAO, IBookedTimeFrameDAO bookedTimeFrameDAO, IItemToRoomDAO itemToRoomDAO, IItemToWorkspaceDAO itemToWorkspaceDAO, IWorkspaceDAO workspaceDAO) {
        this.roomDAO = roomDAO;
        this.itemDAO = itemDAO;
        this.bookedTimeFrameDAO = bookedTimeFrameDAO;
        this.itemToRoomDAO = itemToRoomDAO;
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
        List<BookedTimeframeDTO> book = IterableSupport.toList(bookedTimeFrameDAO.findAll());

        // Map
        List<RoomOOP> result = new ArrayList<>();
        for (RoomDTO room : roomList) {
            /*
            List<ItemToRoomDTO> matchingMaps = itemToWorkspaceList.stream()
                    .filter(itemMapEntry -> itemMapEntry.roomId().equals(room.id()))
                    .toList();

             */
            List<BookedTimeframe> bookedTimeframes = book.stream()
                    .filter(timeframe -> timeframe.roomId().equals(room.id()))
                    .map(timeframe -> new BookedTimeframe(timeframe.dayOfWeek(), timeframe.localTime(), timeframe.duration()))
                    .toList();

            List<WorkspacesDTO> matchingWorkspaces = workspacesList.stream()
                    .filter(workspace -> workspace.roomId().equals(room.id()))
                    .toList();

            List<ItemName> matchingItems = new ArrayList<>();
            List<IWorkspace> workspaces = new ArrayList<>();

            for (WorkspacesDTO workspace : matchingWorkspaces) {
                List<ItemToWorkspaceDTO> matchingItemToWorkspaces = itemToWorkspaceList.stream()
                        .filter(itemMapEntry -> itemMapEntry.workspaceId().equals(workspace.id()))
                        .toList();

                for (ItemToWorkspaceDTO itemToWorkspace : matchingItemToWorkspaces) {
                    matchingItems = itemList.stream()
                            .filter(item -> matchingItemToWorkspaces.stream().anyMatch(
                                    map -> map.itemName().equals(item.itemName())
                            ))
                            .map(item -> new ItemName(item.itemName()))
                            .toList();

                    workspaces.add(new Workspace(workspace.id(), workspace.workspaceNumber(), matchingItems));
                }
            }

            result.add(new RoomOOP(room.id(), room.roomNumber(), bookedTimeframes, workspaces));
        }
        return result;
    }

    @Override
    public IRoom findRoomByID(UUID roomID) throws NotFoundRepositoryException {
        Optional<RoomDTO> room = roomDAO.findById(roomID);
        if (room.isEmpty())
            throw new NotFoundRepositoryException();
        List<String> itemToRoomMaps = itemToRoomDAO.findByRoomId(roomID).stream().map(ItemToRoomDTO::itemName).toList();
        List<ItemName> byItemNames;
        if (!itemToRoomMaps.isEmpty()) {
            byItemNames = itemDAO.findByItemNames(itemToRoomMaps).stream()
                    .map(ItemDTO::toItemName)
                    .toList();
        } else {
            byItemNames = Collections.emptyList();
        }
        List<BookedTimeframe> timeframes = bookedTimeFrameDAO.findByRoomId(roomID).stream()
                .map(BookedTimeframeDTO::toBookedTimeFrame)
                .toList();
        //return new RoomOOP(roomID, room.get().roomNumber(), byItemNames, timeframes);
        return (IRoom) new RuntimeException();
    }

    @Override
    public void remove(UUID roomID) {
        roomDAO.deleteById(roomID);
        // TODO delete reference in ItemToRoom using Database Cascade on delete constraint
    }

    @Override
    public void add(IRoom room) {

        List<ItemName> itemNames = room.getItemNames();
        System.out.println(room.getRoomID());
//        roomDAO.save(new RoomDTO(room.getRoomID(), room.getRoomNumber()));
        roomDAO.insert(room.getRoomID(), room.getRoomNumber());
        room.getBookedTimeframes().forEach(x -> {
            bookedTimeFrameDAO.insert(UUID.randomUUID(), x.day(), x.startTime(), x.duration(), room.getRoomID());
        });
        itemNames.forEach(x -> {
            itemToRoomDAO.insert(UUID.randomUUID(), x.type(), room.getRoomID());
        });
    }

    @Override
    public void addBooking(BookedTimeframe bookedTimeframe, IRoom room) {
        bookedTimeFrameDAO.insert(UUID.randomUUID(), bookedTimeframe.day(), bookedTimeframe.startTime(), bookedTimeframe.duration(), room.getRoomID());
    }

    @Override
    public void addItem(ItemName itemName, IRoom iRoom) {
        itemToRoomDAO.insert(UUID.randomUUID(),itemName.type(),iRoom.getRoomID());
    }

    @Override
    public void removeItem(ItemName itemName, IRoom iRoom) {
        itemToRoomDAO.delete(itemName.type(), iRoom.getRoomID());
    }


}
