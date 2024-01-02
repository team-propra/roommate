package com.example.roommate.domain.models.entities;


import com.example.roommate.values.domainValues.CalendarDay;
import com.example.roommate.values.domainValues.CalendarDays;
import com.example.roommate.interfaces.entities.IRoom;
import com.example.roommate.values.domainValues.ItemName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Room implements IRoom {

    private final UUID roomID;
    private final String roomnumber;

    public CalendarDays calendarDays;
    

    private final List<ItemName> itemNameList = new ArrayList<>();

    public Room(UUID roomID, String roomnumber) {
        this.roomID = roomID;
        this.roomnumber = roomnumber;
        this.calendarDays = new CalendarDays();
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

    @Override
    public CalendarDays getCalendarDays() {
        return calendarDays;
    }

    @Override
    public boolean isAvailable(String weekday, String startUhrzeit, String endUhrzeit) {
        // Use Reflection
        Class<?> calendarDaysClass = CalendarDays.class;
        try {
            Method method = calendarDaysClass.getMethod(weekday);
            Object result = method.invoke(this.calendarDays);

            if (result instanceof CalendarDay) {
                CalendarDay day = (CalendarDay) result;
                return day.isAvailable(startUhrzeit, endUhrzeit);
            }
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    @Override
    public String toString() {
        return "Room " + roomnumber + " contains =" + itemNameList;
    }
}
