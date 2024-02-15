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
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestClass
class RoomDomainServiceTest {

    RoomRepository roomRepository = new RoomRepository();
    ItemRepository itemRepository = new ItemRepository();
    RoomDomainService roomDomainService = ServiceFactory.createRoomService(roomRepository, itemRepository);

    UUID roomID = ValuesFactory.id;
    UUID differentRoomID = UUID.fromString("6d5bbffd-96eb-4475-9095-5f6ba653f118");
    RoomApplicationData room = new RoomApplicationData(roomID, new RoomNumber("101"));
    RoomApplicationData differentRoom = new RoomApplicationData(differentRoomID, new RoomNumber("102"));

    @Test
    @DisplayName("testing the addRoom function")
    void test_1() {


        roomDomainService.addRoom(room);

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room.roomID());
    }

    @Test
    @DisplayName("testing the removeRoom function")
    void test_2_1() {

        roomDomainService.addRoom(room);

        roomDomainService.removeRoom(room);


        RoomEntry roomEntry = new RoomEntry(room.roomID(), room.roomNumber(), null, null);
        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).doesNotContain(roomEntry.getRoomID());
    }

    @Test
    @DisplayName("testing the removeRoom function when the room is not in the RoomRepository")
    void test_2_2() {


        roomDomainService.addRoom(room);

        roomDomainService.removeRoom(differentRoom);

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room.roomID());
    }

    @Test
    @DisplayName("testing the getRooms function")
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
    @DisplayName("testing the findRoomByID function")
    void test_4_1() throws NotFoundRepositoryException {
        roomDomainService.addRoom(room);

        assertThatCode(() -> roomDomainService.findRoomByID(room.roomID()))
                .doesNotThrowAnyException();
        IRoom roomByID = roomDomainService.findRoomByID(room.roomID());
        assertThat(new RoomApplicationData(roomByID.getRoomID(), roomByID.getRoomNumber())).isEqualTo(room);
    }

    @Test
    @DisplayName("testing the findRoomByID function when there is no room there")
    void test_4_2() {

        roomDomainService.addRoom(room);

        assertThatThrownBy(() -> roomDomainService.findRoomByID(differentRoomID)).isInstanceOf(NotFoundRepositoryException.class);
    }

    @Test
    @DisplayName("testing the saveAll function")
    @Disabled
    void test_5() {
        Room room1 = new Room(roomID, new RoomNumber("106"));
        Room room2 = new Room(differentRoomID, new RoomNumber("107"));

        //   roomDomainService.saveAll(List.of(room1, room2));

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).contains(room1.getRoomID(), room2.getRoomID());
    }

    @DisplayName("Adding a room that is already in the List does not change it")
    @Test
    @Disabled
    void test_6() {
        Room room = new Room(roomID, new RoomNumber("106"));

        //  roomDomainService.saveAll(List.of(room));
        roomDomainService.addRoom(new RoomApplicationData(roomID, room.getRoomNumber()));

        assertThat(roomRepository.findAll().stream().map(IRoom::getRoomID)).containsOnlyOnce(room.getRoomID());
    }

}

