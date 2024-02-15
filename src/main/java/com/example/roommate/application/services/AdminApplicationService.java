package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.values.domainValues.RoomNumber;
import com.example.roommate.values.forms.RoomDataForm;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.UUID;

@SuppressFBWarnings(value="EI2", justification="RoomDomainService is properly injected")
@ApplicationService

public class AdminApplicationService {

    final RoomDomainService roomDomainService;

    public AdminApplicationService(RoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;
    }

    public void addRoom(RoomDataForm roomDataForm){
        UUID uuid = UUID.randomUUID();
        RoomApplicationData roomApplicationData = new RoomApplicationData(uuid, new RoomNumber(roomDataForm.roomNumber()));
        roomDomainService.addRoom(roomApplicationData);
    }
}
