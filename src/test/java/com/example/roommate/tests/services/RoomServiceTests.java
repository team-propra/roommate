package com.example.roommate.tests.services;

import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.data.RoomEntry;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.interfaces.values.ItemName;
import com.example.roommate.persistence.ItemRepository;
import com.example.roommate.persistence.RoomRepository;
import com.example.roommate.persistence.exceptions.NotFoundRepositoryException;
import com.example.roommate.services.RoomService;
import com.example.roommate.tests.factories.EntityFactory;
import com.example.roommate.tests.factories.ServiceFactory;
import com.example.roommate.tests.factories.ValuesFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class RoomServiceTest {

    RoomRepository roomRepository = new RoomRepository();
    ItemRepository itemRepository = new ItemRepository();
    RoomService roomService = ServiceFactory.createRoomService(roomRepository, itemRepository);

    UUID roomID = UUID.fromString("f2bf727f-249c-482b-b3ee-11eb2659cb7e");
    UUID differentRoomID = UUID.fromString("6d5bbffd-96eb-4475-9095-5f6ba653f118");

    @Test
    @DisplayName("testing the addRoom function")
    void test_1() {
        

        Room room = new Room(roomID, "101");
        roomService.addRoom(room);

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber());
        assertThat(roomRepository.findAll()).contains(roomEntry);
    }

    @Test
    @DisplayName("testing the removeRoom function")
    void test_2_1() {

        Room room = new Room(roomID, "102");
        roomService.addRoom(room);

        roomService.removeRoom(room);


        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber());
        assertThat(roomRepository.findAll()).doesNotContain(roomEntry);
    }

    @Test
    @DisplayName("testing the removeRoom function when the room is not in the RoomRepository")
    void test_2_2() {
        

        Room room = new Room(roomID, "102");
        Room differentRoom = new Room(differentRoomID, "102");

        roomService.addRoom(room);

        roomService.removeRoom(differentRoom);

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber());
        assertThat(roomRepository.findAll()).contains(roomEntry);
    }

    @Test
    @DisplayName("testing the getRooms function")
    void test_3() {
        Room room1 = new Room(roomID, "103");
        Room room2 = new Room(differentRoomID, "104");
        ArrayList<IRoom> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        RoomRepository roomRepository = new RoomRepository(rooms);
        RoomService roomService = ServiceFactory.createRoomService(roomRepository, itemRepository);

        //one room already exists
        assertThat(roomService.getRooms().size()).isEqualTo(2);
        assertThat(roomService.getRooms()).contains(room1, room2);
    }

    @Test
    @DisplayName("testing the findRoomByID function")
    void test_4_1() throws NotFoundRepositoryException {
        Room room = new Room(roomID, "105");
        roomService.addRoom(room);

        assertThatCode(()->roomService.findRoomByID(room.getRoomID()))
                .doesNotThrowAnyException();
        assertThat(roomService.findRoomByID(room.getRoomID())).isEqualTo(room);
    }

    @Test
    @DisplayName("testing the findRoomByID function when there is no room there")
    void test_4_2() {

        Room room = new Room(roomID, "105");
        roomService.addRoom(room);

        assertThatThrownBy(()->roomService.findRoomByID(differentRoomID)).isInstanceOf(NotFoundRepositoryException.class);
    }

    @Test
    @DisplayName("testing the saveAll function")
    void test_5() {
        Room room1 = new Room(roomID, "106");
        Room room2 = new Room(differentRoomID, "107");

        roomService.saveAll(List.of(room1, room2));

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room1.getRoomID(), room2.getRoomID());
    }

    @DisplayName("Adding a room that is already in the List does not change it")
    @Test
    void test_6() {
        Room room = new Room(roomID, "106");

        roomService.saveAll(List.of(room));
        roomService.addRoom(room);

        assertThat(roomRepository.findAll()).containsOnlyOnce(new RoomEntry(room.getRoomID(), room.getRoomNumber()));
    }

    @DisplayName("Can find a room with an item")
    @Test
    @Disabled
    void test_7() {
        Room room = EntityFactory.createRoom();
        ItemName itemName = ValuesFactory.createItemName("chair");
        roomService.saveAll(List.of(room));
        room.addItem(itemName);

        List<Room> resultList = roomService.findRoomsWithItem(List.of(itemName));
        System.out.println(room.getItems());

        assertThat(resultList).contains(room);
    }
}

