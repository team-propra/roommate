package com.example.roommate.tests.controller.home;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class HomeControllerTest extends ControllerHttpFixtureTest {
    @Test
    void verifiedUserWithAKeyCanOpenTheDashboard() throws Exception {
        when(authenticationApplicationService.tryEnsureUserKey("verified")).thenReturn(true);
        when(bookingApplicationService.getRoomHomeModels(any(Locale.class))).thenReturn(List.of());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.opensPublicDashboard()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }

    @Test
    void guestCanOpenTheDashboardAsAPublicRoommateEntryPoint() throws Exception {
        when(bookingApplicationService.getRoomHomeModels(any(Locale.class))).thenReturn(List.of());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensPublicDashboard()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }

    @Test
    void germanIsRenderedByDefaultWhenNoLanguageCookieExists() throws Exception {
        when(bookingApplicationService.getRoomHomeModels(any(Locale.class))).thenReturn(List.of());
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve("/")).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.body()).contains("Willkommen bei Roommate");
        assertThat(response.body()).contains("Arbeitsplätze");
    }

    @Test
    void englishCanBeSelectedAndPersistedInTheLanguageCookie() throws Exception {
        when(bookingApplicationService.getRoomHomeModels(any(Locale.class))).thenReturn(List.of());
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> englishResponse = client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve("/?lang=en")).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );
        String languageCookie = englishResponse.headers().allValues("set-cookie").stream()
                .filter(cookie -> cookie.startsWith("roommate_language="))
                .findFirst()
                .orElseThrow();

        HttpResponse<String> persistedResponse = client.send(
                HttpRequest.newBuilder(URI.create(scenario.baseUri() + "/"))
                        .header("Cookie", languageCookie)
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(englishResponse.body()).contains("Welcome to Roommate");
        assertThat(languageCookie).contains("roommate_language=en");
        assertThat(persistedResponse.body()).contains("Workspaces");
    }
}
