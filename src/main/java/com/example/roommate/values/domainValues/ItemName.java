package com.example.roommate.values.domainValues;

public record ItemName(String type) {
    @Override
    public String toString() {
        return type;
    }
}
