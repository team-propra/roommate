package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.interfaces.application.services.IAdminApplicationService;
import com.example.roommate.interfaces.domain.services.IRoomDomainService;
import com.example.roommate.values.domainValues.RoomNumber;
import com.example.roommate.values.forms.RoomDataForm;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.UUID;

@SuppressFBWarnings(value="EI2", justification="RoomDomainService is properly injected")
@ApplicationService

public class AdminApplicationService implements IAdminApplicationService {

    final IRoomDomainService roomDomainService;

    public AdminApplicationService(IRoomDomainService roomDomainService) {
        this.roomDomainService = roomDomainService;
    }

    public void addRoom(RoomDataForm roomDataForm){
        UUID uuid = UUID.randomUUID();
        RoomApplicationData roomApplicationData = new RoomApplicationData(uuid, new RoomNumber(roomDataForm.roomNumber()));
        roomDomainService.addRoom(roomApplicationData);
    }
}
