package com.example.roommate.repositories;

import com.example.roommate.tests.domain.entities.ItemName;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {

    List<ItemName> itemList = new ArrayList<>();

    public ItemRepository() {
        itemList.addAll(List.of(new ItemName("Table"), new ItemName("HDMI Cable"), new ItemName("Desk"), new ItemName("Chair")));
    }

    public List<ItemName> getItems() {
        return itemList;
    }
}

