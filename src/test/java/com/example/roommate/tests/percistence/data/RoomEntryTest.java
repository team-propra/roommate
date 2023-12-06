package com.example.roommate.tests.percistence.data;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.persistence.data.RoomEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@TestClass
class RoomEntryTest {

    UUID id = ValuesFactory.id;

    @Test
    @DisplayName("Get Room ID Test")
    void getRoomID() {
        RoomEntry roomEntry = ValuesFactory.createRoomEntry();

        assertThat(roomEntry.getRoomID()).isEqualTo(id);
    }

    @Test
    @DisplayName("Get Room Number Test")
    void getRoomNumber() {
        String expectedRoomNumber = "14";
        RoomEntry roomEntry = ValuesFactory.createRoomEntry(expectedRoomNumber);

        assertThat(roomEntry.getRoomNumber()).isEqualTo(expectedRoomNumber);
    }

    @Test
    @DisplayName("Get Item Names Test")
    void getItemNames() {
        RoomEntry roomEntry = ValuesFactory.createRoomEntry();

        assertThatThrownBy(roomEntry::getItemNames).isInstanceOf(UnsupportedOperationException.class);
    }
}

