package com.example.roommate.persistence.postgres;

import com.example.roommate.values.domainValues.ItemName;
import org.springframework.data.repository.CrudRepository;

public interface ItemDAO extends CrudRepository<ItemDTO, String> {
}
