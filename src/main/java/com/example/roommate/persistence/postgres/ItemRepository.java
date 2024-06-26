package com.example.roommate.persistence.postgres;

import com.example.roommate.interfaces.repositories.IItemRepository;
import com.example.roommate.utility.IterableSupport;
import com.example.roommate.values.domainValues.ItemName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("!test")
@Repository
@SuppressFBWarnings(value="EI2", justification="DAO injection required for database interaction")
public class ItemRepository implements IItemRepository {
    IItemDAO itemDAO;

    public ItemRepository(IItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public List<ItemName> getItems() {
       return IterableSupport.toList(itemDAO.findAll()).stream()
                .map(ItemDTO::toItemName)
                .toList();

    }

    @Override
    public void addItem(ItemName itemName) {
        itemDAO.insert(itemName.type());
    }

    @Override
    public void removeItem(ItemName itemName) {
        itemDAO.delete(itemName.type());
    }


}
