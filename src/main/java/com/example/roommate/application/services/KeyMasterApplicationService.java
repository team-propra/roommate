package com.example.roommate.application.services;

import com.example.roommate.annotations.ApplicationService;

import com.example.roommate.application.data.KeyOwnerApplicationData;
import com.example.roommate.application.data.RoomApplicationData;
import com.example.roommate.domain.services.RoomDomainService;
import com.example.roommate.domain.services.UserDomainService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


@ApplicationService
@SuppressFBWarnings(value="EI2", justification="UserDomainService & RoomDomainService are properly injected")
public class KeyMasterApplicationService {
    UserDomainService userDomainService;
    RoomDomainService roomDomainService;

    @Value("${roommate.key-master-url}")
    String host;

    @Autowired
    public KeyMasterApplicationService(UserDomainService userDomainService, RoomDomainService roomDomainService) {
        this.userDomainService = userDomainService;
        this.roomDomainService = roomDomainService;
    }

    @Scheduled(fixedDelay = 2000)
    public void fetchKeys() {

        List<KeyOwnerApplicationData> keyOwners = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host(host)
                                .port(3000)
                                .path("/key")
                                .build()
                )
                .retrieve()
                .bodyToFlux(KeyOwnerApplicationData.class)
                .collectList()
                .block(Duration.of(8, ChronoUnit.SECONDS));
        if (keyOwners != null) {
            for (KeyOwnerApplicationData keyOwner : keyOwners) {
                userDomainService.verifyUser(UUID.fromString(keyOwner.id()), keyOwner.owner());
            }
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void fetchRooms() {

        List<RoomApplicationData> rooms = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host(host)
                                .port(3000)
                                .path("/room")
                                .build()
                )
                .retrieve()
                .bodyToFlux(RoomApplicationData.class)
                .collectList()
                .block(Duration.of(8, ChronoUnit.SECONDS));
        if (rooms != null) {
            for (RoomApplicationData room : rooms) {
                roomDomainService.addRoom(room);
            }
        }
    }

    public UUID createKey(String login) {
        String response = WebClient.create()
            .post()
            .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host(host)
                .port(3000)
                .path("/key/create")
                .build()
            )
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue("user=" + login)
            .retrieve()
            .bodyToMono(String.class)
            .block(Duration.of(8, ChronoUnit.SECONDS));

        if (response == null) {
            throw new RuntimeException("Key creation failed: empty response");
        }

        String uuidText = extractKeyFromResponse(response);
        return UUID.fromString(uuidText);
    }

    private String extractKeyFromResponse(String responseBody) {
        Document doc = Jsoup.parse(responseBody);
        Element element = doc.getElementById("id");

        if (element == null) {
            throw new RuntimeException("Key ID not found in response");
        }

        return element.text();
    }
}

