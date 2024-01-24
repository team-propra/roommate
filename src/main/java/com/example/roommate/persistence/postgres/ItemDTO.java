package com.example.roommate.persistence.postgres;

import com.example.roommate.values.domainValues.ItemName;
import org.springframework.data.annotation.Id;

public record ItemDTO(@Id String itemName) {
    public ItemName toItemName(){
        return new ItemName(itemName);
    }
}
