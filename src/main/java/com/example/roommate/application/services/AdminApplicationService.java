package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.domain.services.UserDomainService;
import com.example.roommate.interfaces.entities.IUser;
import com.example.roommate.values.domainValues.RoomNumber;
import com.example.roommate.values.forms.RoomDataForm;
import com.example.roommate.values.models.UserModel;
import com.example.roommate.values.models.UsersModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;
import java.util.UUID;

@SuppressFBWarnings(value="EI2", justification="RoomDomainService is properly injected")
@ApplicationService

public class AdminApplicationService {

    final RoomDomainService roomDomainService;
    final UserDomainService userDomainService;

    public AdminApplicationService(RoomDomainService roomDomainService, UserDomainService userDomainService) {
        this.roomDomainService = roomDomainService;
        this.userDomainService = userDomainService;
    }

    public void addRoom(RoomDataForm roomDataForm){
        UUID uuid = UUID.randomUUID();
        RoomApplicationData roomApplicationData = new RoomApplicationData(uuid, new RoomNumber(roomDataForm.roomNumber()));
        roomDomainService.addRoom(roomApplicationData);
    }
    public UsersModel getUsers(){
        List<? extends IUser> allUser = userDomainService.getAllUser();
        List<UserModel> list = allUser.stream().map(x -> new UserModel(x.getHandle(), String.join(", ", x.getRoles()))).toList();
        return new UsersModel(list);
    }
}
