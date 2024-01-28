package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("!test")
@Repository
public class ItemRepository implements IItemRepository {
    @Override
    public List<ItemName> getItems() {
        return null;
    }
}
