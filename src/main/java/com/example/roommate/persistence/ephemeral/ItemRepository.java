package com.example.roommate.persistence.ephemeral;

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
        itemList.add(new ItemName("Table"));
        itemList.add(new ItemName("HDMI Cable"));
        itemList.add(new ItemName("Desk"));
        itemList.add(new ItemName("Chair"));
    }

    public List<ItemName> getItems() {
        return itemList.stream().toList();
    }

    @Override
    public void addItem(ItemName itemName) {
        itemList.add(itemName);
    }
}

