package com.example.roommate.persistence.postgres;

import com.example.roommate.values.domainValues.BookedTimeframe;
import com.example.roommate.values.domainValues.ItemName;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

public record RoomDTO(UUID roomID, String roomNumber, List<ItemDTO> itemList, List<BookedTimeframe> bookedTimeframeList){

    //@GenericGenerator(name = "uuid2", strategy = "uuid2")
}
