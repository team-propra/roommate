package com.example.roommate.domain;

import com.example.roommate.domain.entities.Room;
import com.example.roommate.domain.values.Item;
import com.example.roommate.factories.EntityFactory;
import com.example.roommate.factories.ValuesFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class EntitiesTest {

    @Test
    @DisplayName("add item to room")
    void test_5() {
        Room room = EntityFactory.createRoom();
        Item item = ValuesFactory.createItem("Chair");

        room.addItem(item);

        assertThat(room.getItems()).containsExactly(item);
    }

    @Test
    @DisplayName("Add List of Items to a room")
    void test_6() {
        Room room = EntityFactory.createRoom();
        List<Item> items = List.of(ValuesFactory.createItem("Desk"), ValuesFactory.createItem("Chair"));

        room.addItem(items);

        assertThat(room.getItems()).containsExactlyElementsOf(items);
    }

    @Test
    @DisplayName("Get a List of items when getItems() is called")
    void test_7() {
        Room room = EntityFactory.createRoom();
        Item desk = ValuesFactory.createItem("Desk");
        Item chair = ValuesFactory.createItem("Chair");
        room.addItem(chair);
        room.addItem(desk);

        List<Item> result = room.getItems();

        assertThat(result).containsExactly(chair, desk);
    }
}

