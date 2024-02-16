package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;
import net.minidev.json.JSONArray;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ApplicationService
public class KeyMasterService {

    @Scheduled(fixedDelay = 3000)
    public void fetchEvents() {
       JSONArray jsonArray = WebClient.create()
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
                .bodyToFlux(JSONArray.class)
                .blockLast(Duration.of(8, ChronoUnit.SECONDS));

        for (Object o : jsonArray) {
            System.out.println(o);
        }

       /*
        for (BestellungAbgeschlossen event : events) {
            processEvent(event);
            lastSeen = event.sequenceNumber();
        }

        */
    }
}
