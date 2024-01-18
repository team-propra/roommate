package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.values.forms.RoomDataForm;

import java.util.UUID;

@ApplicationService

public class AdminApplicationService {

    RoomDomainService roomDomainService;

    public AdminApplicationService(RoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;
    }

    public void addRoom(RoomDataForm roomDataForm){
        UUID uuid = UUID.randomUUID();
        RoomApplicationData roomApplicationData = new RoomApplicationData(uuid, roomDataForm.roomNumber());
        roomDomainService.addRoom(roomApplicationData);
    }
}
