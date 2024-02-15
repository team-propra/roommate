package com.example.roommate.tests.percistence.postgres;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.exceptions.persistence.NotFoundRepositoryException;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.persistence.postgres.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@TestClass
@DataJdbcTest
public class RoomRepositoryTest {
    
    @Autowired
    IRoomDAO roomDAO;
    @Autowired
    IItemDAO itemDAO;
    @Autowired
    IBookedTimeFrameDAO bookedTimeFrameDAO;
    @Autowired
    IItemToRoomDAO itemToRoomDAO;
    
    @Test
    @DisplayName("Rooms added to Repository can be retrieved afterwards")
    void test_1() throws NotFoundRepositoryException {
        RoomRepository roomRepository = new RoomRepository(roomDAO,itemDAO,bookedTimeFrameDAO,itemToRoomDAO);
        IRoom room = EntityFactory.createRoom();
        roomRepository.add(room);
        assertThatCode(()->roomRepository.findRoomByID(room.getRoomID())).doesNotThrowAnyException();
    }
}
