package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;

import com.example.roommate.application.data.KeyOwnerApplicationData;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.domain.services.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@ApplicationService
public class KeyMasterApplicationService {
    UserDomainService userDomainService;
    RoomDomainService roomDomainService;

    @Autowired
    public KeyMasterApplicationService(UserDomainService userDomainService, RoomDomainService roomDomainService) {
        this.userDomainService = userDomainService;
        this.roomDomainService = roomDomainService;
    }

    @Scheduled(fixedDelay = 3000)
    public void fetchKeys() {

        List<KeyOwnerApplicationData> keyOwners = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(3000)
                                .path("/key")
                                .build()
                )
                .retrieve()
                .bodyToFlux(KeyOwnerApplicationData.class)
                .collectList()
                .block(Duration.of(8, ChronoUnit.SECONDS));
        if(keyOwners != null) {
            for (KeyOwnerApplicationData keyOwner: keyOwners
            ) {
                System.out.println(keyOwner.owner());
                userDomainService.verifyUser(UUID.fromString(keyOwner.id()), keyOwner.owner());
            }
        }
       }

    @Scheduled(fixedDelay = 3000)
    public void fetchRooms() {

        List<RoomApplicationData> rooms = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(3000)
                                .path("/room")
                                .build()
                )
                .retrieve()
                .bodyToFlux(RoomApplicationData.class)
                .collectList()
                .block(Duration.of(8, ChronoUnit.SECONDS));
        if(rooms != null) {
            for (RoomApplicationData room: rooms) {
                System.out.println(room.roomNumber());
                roomDomainService.addRoom(room);
            }
        }

    }
    }

