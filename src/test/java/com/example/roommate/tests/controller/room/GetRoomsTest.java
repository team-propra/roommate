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
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
}
