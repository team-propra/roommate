package com.example.roommate.persistence.repositories;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("test")
public class ItemRepository implements IItemRepository{

    List<ItemName> itemList = new ArrayList<>();

    public ItemRepository() {
        itemList.addAll(List.of(new ItemName("Table"), new ItemName("HDMI Cable"), new ItemName("Desk"), new ItemName("Chair")));
    }

    public List<ItemName> getItems() {
        return itemList.stream().toList();
    }

    @Override
    public void addItem(ItemName itemName) {
        itemList.add(itemName);
    }
}

