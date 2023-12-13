package com.example.roommate.domain.models.entities;


import com.example.roommate.domain.models.values.CalendarDay;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domain.ItemName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final String roomnumber;

    public CalendarDay monday;//will be replaced by an actual calendar day, prob. using java.time.LocalDate for this, maybe somth. like: List<LocalDate,CalendarDay>
    public CalendarDay tuesday;
    public CalendarDay wednesday;
    public CalendarDay thursday;
    public CalendarDay friday;
    public CalendarDay saturday;
    public CalendarDay sunday;

    private final List<ItemName> itemNameList = new ArrayList<>();

    public Room(UUID roomID, String roomnumber) {
        this.roomID = roomID;
        this.roomnumber = roomnumber;
        this.monday = new CalendarDay();
        this.tuesday = new CalendarDay();
        this.wednesday = new CalendarDay();
        this.thursday = new CalendarDay();
        this.friday = new CalendarDay();
        this.saturday = new CalendarDay();
        this.sunday = new CalendarDay();
    }

    public UUID getRoomID() {
        return roomID;
    }

    public String getRoomNumber() {
        return roomnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomID, room.roomID) && Objects.equals(roomnumber, room.roomnumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, roomnumber);
    }

    public void addItem(ItemName item) {
        itemNameList.add(item);
    }

    public void addItem(List<ItemName> items) {
        itemNameList.addAll(items);
    }

    public List<ItemName> getItemNames() {
        return itemNameList;
    }
}
