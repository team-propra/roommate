package com.example.roommate.tests.services;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.ephemeral.RoomEntry;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.domainValues.RoomNumber;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestClass
class RoomDomainServiceTest {

    RoomRepository roomRepository = new RoomRepository();
    ItemRepository itemRepository = new ItemRepository();
    RoomDomainService roomDomainService = ServiceFactory.createRoomService(roomRepository, itemRepository);

    UUID roomID = ValuesFactory.roomId;
    UUID differentRoomID = UUID.fromString("6d5bbffd-96eb-4475-9095-5f6ba653f118");
    RoomApplicationData room = new RoomApplicationData(roomID, new RoomNumber("101"));
    RoomApplicationData differentRoom = new RoomApplicationData(differentRoomID, new RoomNumber("102"));

    @Test
    @DisplayName("executing addRoom with 1 room makes finAll() correctly containing that 1 room")
    void test_1() {


        roomDomainService.addRoom(room);

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room.roomID());
    }

    @Test
    @DisplayName("adding 1 room followed by removing that 1 room makes findAll not contain that 1 room")
    void test_2_1() {

        roomDomainService.addRoom(room);

        roomDomainService.removeRoom(room);


        RoomEntry roomEntry = new RoomEntry(room.roomID(), room.roomNumber(), null);
        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).doesNotContain(roomEntry.getRoomID());
    }

    @Test
    @DisplayName("executing addRoom with 1 room followed by removing a different room with makes finAll() still correctly contain that 1 room")
    void test_2_2() {


        roomDomainService.addRoom(room);

        roomDomainService.removeRoom(differentRoom);

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room.roomID());
    }

    @Test
    @DisplayName("getRooms properly contains both rooms added to it via add(room)")
    void test_3() {
        Room room1 = new Room(roomID, new RoomNumber("103"));
        Room room2 = new Room(differentRoomID, new RoomNumber("104"));
        ArrayList<IRoom> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        RoomRepository roomRepository = new RoomRepository(rooms);
        RoomDomainService roomDomainService = ServiceFactory.createRoomService(roomRepository, itemRepository);

        //one room already exists
        assertThat(roomDomainService.getRooms().size()).isEqualTo(2);
        assertThat(roomDomainService.getRooms()).contains(room1, room2);
    }

    @Test
    @DisplayName("findRoomByID does not throw any exception if the room is already added AND also returns that room correctly")
    void test_4_1() throws NotFoundRepositoryException {
        roomDomainService.addRoom(room);

        assertThatCode(() -> roomDomainService.findRoomByID(room.roomID()))
                .doesNotThrowAnyException();
        IRoom roomByID = roomDomainService.findRoomByID(room.roomID());
        assertThat(new RoomApplicationData(roomByID.getRoomID(), roomByID.getRoomNumber())).isEqualTo(room);
    }

    @Test
    @DisplayName("after adding room A, findRoomById of a different throws NotFoundRepositoryException as its not there")
    void test_4_2() {

        roomDomainService.addRoom(room);

        assertThatThrownBy(() -> roomDomainService.findRoomByID(differentRoomID)).isInstanceOf(NotFoundRepositoryException.class);
    }


    @Test
    @DisplayName("executing addRoom with room A makes findAll() contain room A")
    void test_5() { //Problem: RoomEnrty.CalenderDay's are Objects, not comparable values like Strings or UUDI's


        Room room = new Room(roomID, new RoomNumber("101"));
        roomDomainService.addRoom(new RoomApplicationData(roomID, room.getRoomNumber()));

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber(), List.of());
        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID))
                .contains(roomEntry.roomID());
    }

    
    

   /* @Test //fixing tests by adding customAssertions
    @DisplayName("testing the removeRoom function")
    void test_2_1() {

        Room room = new Room(id, "102");
        roomService.addRoom(room);

        roomService.removeRoom(room);


        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber());
        assertThat(roomRepository.findAll()).doesNotContain(roomEntry);
    }

    @Test
    @DisplayName("testing the removeRoom function when the room is not in the RoomRepository")
    void test_2_2() {
        

        Room room = new Room(id, "102");
        Room differentRoom = new Room(differentRoomID, "102");

        roomService.addRoom(room);

        roomService.removeRoom(differentRoom);

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber());
        assertThat(roomRepository.findAll()).contains(roomEntry);
    }

    @Test
    @DisplayName("testing the getRooms function")
    void test_3() {
        Room room1 = new Room(id, "103");
        Room room2 = new Room(differentRoomID, "104");
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        RoomRepository roomRepository = new RoomRepository(rooms.stream().map(r -> new RoomEntry(r.getRoomID(), r.getRoomNumber())).toList());
        RoomDomainService roomService = ServiceFactory.createRoomService(roomRepository, itemRepository);

        //one room already exists
        assertThat(roomService.getRooms().size()).isEqualTo(2);
        assertThat(roomService.getRooms()).contains(room1, room2);
    }

    @Test
    @DisplayName("testing the findRoomByID function")
    void test_4_1() throws NotFoundRepositoryException {
        Room room = new Room(id, "105");
        roomService.addRoom(room);

        assertThatCode(()->roomService.findRoomByID(room.getRoomID()))
                .doesNotThrowAnyException();
        assertThat(roomService.findRoomByID(room.getRoomID())).isEqualTo(room);
    }

    @Test
    @DisplayName("testing the findRoomByID function when there is no room there")
    void test_4_2() {

        Room room = new Room(id, "105");
        roomService.addRoom(room);

        assertThatThrownBy(()->roomService.findRoomByID(differentRoomID)).isInstanceOf(NotFoundRepositoryException.class);
    }

    @Test
    @DisplayName("testing the saveAll function")
    void test_5() {
        Room room1 = new Room(id, "106");
        Room room2 = new Room(differentRoomID, "107");

        roomService.saveAll(List.of(room1, room2));

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(new RoomEntry(room1.getRoomID(), room1.getRoomNumber()), new RoomEntry(room2.getRoomID(), room2.getRoomNumber()));
    }

    @DisplayName("Adding a room that is already in the List does not change it")
    @Test
    void test_6() {
        Room room = new Room(id, "106");

        roomService.saveAll(List.of(room));
        roomService.addRoom(room);

        assertThat(roomRepository.findAll()).containsOnlyOnce(new RoomEntry(room.getRoomID(), room.getRoomNumber()));
    }*/
}

