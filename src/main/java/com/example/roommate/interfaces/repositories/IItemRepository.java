package com.example.roommate.interfaces.repositories;

import com.example.roommate.interfaces.values.ItemName;

import java.util.List;

public interface IItemRepository {
    List<ItemName> getItems();
}
