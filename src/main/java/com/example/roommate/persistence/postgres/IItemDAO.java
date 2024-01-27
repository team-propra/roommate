package com.example.roommate.persistence.postgres;

import com.example.roommate.annotations.Interface;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Interface
public interface IItemDAO extends CrudRepository<ItemDTO, String> {
    List<ItemDTO> findByItemName(String itemName);
    @Query("SELECT * FROM items WHERE itemName IN (:itemNames)")
    List<ItemDTO> findByItemNames(List<String> itemNames);
}
