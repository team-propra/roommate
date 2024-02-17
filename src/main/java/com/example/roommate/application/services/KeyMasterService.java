package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;

import com.example.roommate.domain.services.UserDomainService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationService
public class KeyMasterService {
    UserDomainService userDomainService;

    @Scheduled(fixedDelay = 3000)
    public void fetchKeys() {

        List<KeyOwner> list = WebClient.create()
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
                .bodyToFlux(KeyOwner.class)
                .collectList()
                .block(Duration.of(8, ChronoUnit.SECONDS));
        for (KeyOwner keyOwner: list
             ) {
            System.out.println(keyOwner.owner());
            userDomainService.verifyUser(keyOwner.key(), keyOwner.owner());
        }
       }
    }

