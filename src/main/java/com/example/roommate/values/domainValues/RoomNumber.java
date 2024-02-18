package com.example.roommate.values.domainValues;

public record RoomNumber(String number) {
    @Override
    public String toString() {
        return "RoomNumber" + number;
    }
}
