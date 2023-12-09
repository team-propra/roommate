package com.example.roommate.interfaces.repositories;

import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.values.domain.ItemName;

import java.util.List;

@RepositoryInterface
public interface IItemRepository {
    List<ItemName> getItems();
}
