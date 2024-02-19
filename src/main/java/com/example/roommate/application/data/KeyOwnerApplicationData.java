package com.example.roommate.application.data;

import com.example.roommate.annotations.ApplicationData;


@ApplicationData
public record KeyOwnerApplicationData(String id, String owner) {
}
