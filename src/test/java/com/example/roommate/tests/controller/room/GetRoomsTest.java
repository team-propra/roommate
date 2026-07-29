package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.ItemModel;
import com.example.roommate.values.models.RoomBookingModel;
import com.example.roommate.values.models.RoomSearchModel;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.xcepto.xceptoj.Xcepto;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class GetRoomsTest extends ControllerHttpFixtureTest {
    @Test
    void guestCanOpenWorkspaceSearch() throws Exception {
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel("", "", "", List.of(), List.of(), List.of()));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspaceSearch()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }

    @Test
    void adminCanReachRoomProvisioningFromWorkspaceSearch() throws Exception {
        String expectedProvisioningLink = "/rooms/add";
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("admin")))
                .thenReturn(new RoomSearchModel("", "", "", List.of(), List.of(), List.of()));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensWorkspaceSearch()
                    .assertSuccess()
                    .assertThatResponseContentString(html -> html.contains(expectedProvisioningLink));
        }, TIMEOUT, STEP);
    }

    @Test
    void adminCanOpenTheAddRoomForm() throws Exception {
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensAddRoomForm()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }

    @Test
    void guestCanSearchTheWorkspaceInventoryByTimeAndEquipment() throws Exception {
        String date = "2026-07-06";
        String startTime = "09:00";
        String endTime = "10:00";
        String requestedEquipment = "Monitor";
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenAnswer(invocation -> {
                    List<?> selectedItems = invocation.getArgument(0);
                    if (selectedItems == null || !selectedItems.contains(requestedEquipment)) {
                        return new RoomSearchModel("", "", "", List.of(), List.of(), List.of());
                    }

                    return new RoomSearchModel(
                            date,
                            startTime,
                            endTime,
                            List.of(new ItemModel(requestedEquipment), new ItemModel("Dock")),
                            List.of(requestedEquipment),
                            List.of(new RoomBookingModel(ROOM_ID, WORKSPACE_ID, workspaceNumber, roomNumber, List.of(new ItemName(requestedEquipment))))
                    );
                });
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspaceSearch()
                    .assertSuccess();

            roommate.searchesForWorkspaces(date, startTime, endTime, requestedEquipment)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(roomNumber)
                                    && html.contains(String.valueOf(workspaceNumber))
                                    && html.contains(requestedEquipment));
        }, TIMEOUT, STEP);
    }

    @Test
    void submittedSearchFilterIsStoredInACookie() throws Exception {
        String date = "2026-07-06";
        String startTime = "09:00";
        String endTime = "10:00";
        String requestedEquipment = "Monitor";
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel("", "", "", List.of(), List.of(), List.of()));
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve(
                                "/rooms?datum=%s&startUhrzeit=%s&endUhrzeit=%s&gegenstaende=%s"
                                        .formatted(date, startTime, endTime, requestedEquipment)
                        ))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.headers().allValues("set-cookie"))
                .anyMatch(cookie -> cookie.startsWith("roommate_search_filter=")
                        && cookie.contains("datum=2026-07-06")
                        && cookie.contains("startUhrzeit=09%3A00")
                        && cookie.contains("endUhrzeit=10%3A00")
                        && cookie.contains("gegenstaende=Monitor")
                        && cookie.contains("HttpOnly")
                        && cookie.contains("Secure")
                        && cookie.contains("SameSite=Lax"));
    }

    @Test
    void savedSearchFilterCookieIsAppliedOnTheNextWorkspaceSearchVisit() throws Exception {
        String cookieValue = "roommate_search_filter=datum=2026-07-06&startUhrzeit=09%3A00&endUhrzeit=10%3A00&gegenstaende=Monitor";
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel("", "", "", List.of(), List.of(), List.of()));
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve("/rooms"))
                        .header("Cookie", cookieValue)
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        ArgumentCaptor<List<String>> items = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<SearchTimeForm> timeForm = ArgumentCaptor.forClass(SearchTimeForm.class);
        verify(bookingApplicationService).getRoomSearchModel(items.capture(), timeForm.capture(), eq("__guest__"));
        assertThat(items.getValue()).containsExactly("Monitor");
        assertThat(timeForm.getValue().datum()).isEqualTo("2026-07-06");
        assertThat(timeForm.getValue().startUhrzeit()).isEqualTo("09:00");
        assertThat(timeForm.getValue().endUhrzeit()).isEqualTo("10:00");
    }

    @Test
    void malformedSearchFilterCookieFallsBackToDefaultWorkspaceSearch() throws Exception {
        String cookieValue = "roommate_search_filter=datum=%";
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel("", "", "", List.of(), List.of(), List.of()));
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve("/rooms"))
                        .header("Cookie", cookieValue)
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        ArgumentCaptor<SearchTimeForm> timeForm = ArgumentCaptor.forClass(SearchTimeForm.class);
        verify(bookingApplicationService).getRoomSearchModel(any(), timeForm.capture(), eq("__guest__"));
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(timeForm.getValue().datum()).isEqualTo("2024-01-01");
        assertThat(timeForm.getValue().startUhrzeit()).isEqualTo("08:00");
        assertThat(timeForm.getValue().endUhrzeit()).isEqualTo("16:00");
    }

    @Test
    void languageSwitcherPreservesActiveWorkspaceSearchFilters() throws Exception {
        String date = "2026-07-06";
        String startTime = "09:00";
        String endTime = "10:00";
        String requestedEquipment = "Monitor";
        when(bookingApplicationService.getRoomSearchModel(any(), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel(date, startTime, endTime, List.of(), List.of(requestedEquipment), List.of()));
        var scenario = roommateIsRunning();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(scenario.baseUri().resolve(
                                "/rooms?datum=%s&startUhrzeit=%s&endUhrzeit=%s&gegenstaende=%s"
                                        .formatted(date, startTime, endTime, requestedEquipment)
                        ))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(response.body()).contains("href=\"/rooms?datum=2026-07-06&amp;startUhrzeit=09%3A00&amp;endUhrzeit=10%3A00&amp;gegenstaende=Monitor&amp;lang=en\"");
        assertThat(response.body()).contains("href=\"/rooms?datum=2026-07-06&amp;startUhrzeit=09%3A00&amp;endUhrzeit=10%3A00&amp;gegenstaende=Monitor&amp;lang=de\"");
    }
}
