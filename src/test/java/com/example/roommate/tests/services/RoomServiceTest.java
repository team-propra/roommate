package com.example.roommate.tests.services;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.models.entities.Room;

import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.factories.ServiceFactory;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.ephemeral.RoomEntry;
import com.example.roommate.persistence.ephemeral.ItemRepository;
import com.example.roommate.persistence.ephemeral.RoomRepository;
import com.example.roommate.values.domainValues.RoomNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestClass
class RoomServiceTest {

    RoomRepository roomRepository = new RoomRepository();
    ItemRepository itemRepository = new ItemRepository();
    RoomDomainService roomService = ServiceFactory.createRoomService(roomRepository, itemRepository);

    UUID roomID = UUID.fromString("f2bf727f-249c-482b-b3ee-11eb2659cb7e");

    @Test
    @DisplayName("testing the addRoom function")
    void test_1() { //Problem: RoomEnrty.CalenderDay's are Objects, not comparable values like Strings or UUDI's
        

        Room room = new Room(roomID, new RoomNumber("101"));
        roomService.addRoom(new RoomApplicationData(roomID, room.getRoomNumber()));

        RoomEntry roomEntry = new RoomEntry(room.getRoomID(), room.getRoomNumber(), List.of(), List.of());
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

