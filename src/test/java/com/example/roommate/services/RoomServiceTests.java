package com.example.roommate.services;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.repositories.RoomRepository;
import com.example.roommate.repositories.exceptions.NotFoundRepositoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class RoomServiceTest {

    @Test
    @DisplayName("testing the addRoom function")
    void test_1() {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room = new Room(UUID.randomUUID(), "101");
        roomService.addRoom(room);

        assertThat(roomRepository.findAll()).contains(room);
    }

    @Test
    @DisplayName("testing the removeRoom function")
    void test_2_1() {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room = new Room(UUID.randomUUID(), "102");
        roomService.addRoom(room);

        roomService.removeRoom(room);

        assertThat(roomRepository.findAll()).doesNotContain(room);
    }

    @Test
    @DisplayName("testing the removeRoom function when the room is not in the RoomRepository")
    void test_2_2() {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room = new Room(UUID.randomUUID(), "102");
        Room differentRoom = new Room(UUID.randomUUID(), "102");

        roomService.addRoom(room);

        roomService.removeRoom(differentRoom);

        assertThat(roomRepository.findAll()).contains(room);
    }

    @Test
    @DisplayName("testing the getRooms function")
    void test_3() {
        Room room1 = new Room(UUID.randomUUID(), "103");
        Room room2 = new Room(UUID.randomUUID(), "104");
        ArrayList<Room> rooms = new ArrayList<>();
        RoomRepository roomRepository = new RoomRepository(rooms);
        RoomService roomService = new RoomService(roomRepository);

        

        roomService.addRoom(room1);
        roomService.addRoom(room2);

        //one room already exists
        assertThat(roomService.getRooms().size()).isEqualTo(2);
        assertThat(roomService.getRooms()).contains(room1, room2);
    }

    @Test
    @DisplayName("testing the findRoomByID function")
    void test_4_1() throws NotFoundRepositoryException {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room = new Room(UUID.randomUUID(), "105");
        roomService.addRoom(room);

        assertThatCode(()->roomService.findRoomByID(room.getRoomID()))
                .doesNotThrowAnyException();
        assertThat(roomService.findRoomByID(room.getRoomID())).isEqualTo(room);
                
    }

    @Test
    @DisplayName("testing the findRoomByID function when there ")
    void test_4_2() {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room = new Room(UUID.randomUUID(), "105");
        roomService.addRoom(room);

        UUID randomRoomID = UUID.randomUUID();
        assertThatThrownBy(()->roomService.findRoomByID(randomRoomID)).isInstanceOf(NotFoundRepositoryException.class);
    }

    @Test
    @DisplayName("testing the saveAll function")
    void test_5() {
        RoomRepository roomRepository = new RoomRepository();
        RoomService roomService = new RoomService(roomRepository);

        Room room1 = new Room(UUID.randomUUID(), "106");
        Room room2 = new Room(UUID.randomUUID(), "107");

        roomService.saveAll(List.of(room1, room2));

        assertThat(roomRepository.findAll()).contains(room1, room2);
    }
}

