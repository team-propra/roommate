package com.example.roommate.tests.domain;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.domain.models.entities.Admin;
import com.example.roommate.domain.models.entities.Room;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.domain.models.entities.Workspace;
import com.example.roommate.examples.Officer;
import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import com.example.roommate.values.domainValues.RoomNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@TestClass
public class EntitiesTest {

    @DisplayName("Can create an Admin")
    @Test
    void test_1() {
        Admin admin = EntityFactory.createAdmin();
        assertThat(admin).isInstanceOf(Admin.class);
    }


    @DisplayName("Room has correct room number")
    @Test
    void test_3() {
        RoomNumber roomNumber = new RoomNumber("12");
        Room room = EntityFactory.createRoom(roomNumber);
        assertThat(room.getRoomNumber().number()).isEqualTo(roomNumber.number());
    }

    @DisplayName("Can create a User")
    @Test
    void test_4() {
        User user = EntityFactory.createUser();
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    @DisplayName("add item to workspace results in getItems return said item")
    void test_5() {
        Workspace workspace = EntityFactory.createWorkspace();
        ItemName item = ValuesFactory.createItemName("Chair");

        workspace.addItem(item);

        assertThat(workspace.getItems()).containsExactly(item);
    }

    @Test
    @DisplayName("Add List of Items to a workspace make getItems() yield all of these")
    void test_6() {
        Workspace workspace = EntityFactory.createWorkspace();
        List<ItemName> items = List.of(ValuesFactory.createItemName("Desk"), ValuesFactory.createItemName("Chair"));

        workspace.addItem(items);

        assertThat(workspace.getItems()).containsExactlyElementsOf(items);
    }

    @Test
    @DisplayName("getItems() returns a list of the exact items, added to a workspace")
    void test_7() {
        Workspace workspace = EntityFactory.createWorkspace();
        ItemName desk = ValuesFactory.createItemName("Desk");
        ItemName chair = ValuesFactory.createItemName("Chair");
        workspace.addItem(chair);
        workspace.addItem(desk);

        List<ItemName> result = workspace.getItems();

        assertThat(result).containsExactly(chair, desk);
    }

    @DisplayName("A workspace is available at a timeslot (A) if the workspace contains a prior timeslot (B), that ends at the exact time of (A)'s starting time")
    @Test
    void test_8() {
        Workspace workspace = Officer.Workspace();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(3));
        workspace.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));

        boolean available = workspace.isAvailable(desiredSlot);
        assertThat(available).isTrue();
    }

    @DisplayName("A workspace is available at a timeslot (A) if the workspace contains a later timeslot (B), that starts at the exact time of (A)'s end time")
    @Test
    void test_9() {
        Workspace workspace = Officer.Workspace();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));
        workspace.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(3));

        boolean available = workspace.isAvailable(desiredSlot);
        assertThat(available).isTrue();
    }

    @DisplayName("A room is not available if the booked timeslot before overlaps with the desired one")
    @Test
    void test_10() {
        Workspace workspace = Officer.Workspace();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(4));
        workspace.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));

        boolean available = workspace.isAvailable(desiredSlot);
        assertThat(available).isFalse();
    }

    @DisplayName("A room is not available if a latter booked timeslot overlaps with the duration of desired one")
    @Test
    void test_11() {
        Workspace workspace = Officer.Workspace();
        BookedTimeframe bookedTimeframe = ValuesFactory.createBookedTimeframe(LocalTime.of(11, 0), Duration.ofHours(3));
        workspace.addBookedTimeframe(bookedTimeframe);
        BookedTimeframe desiredSlot = ValuesFactory.createBookedTimeframe(LocalTime.of(8, 0), Duration.ofHours(4));

        boolean available = workspace.isAvailable(desiredSlot);
        assertThat(available).isFalse();
    }

}

