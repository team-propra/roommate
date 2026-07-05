package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.values.domainValues.ItemName;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.ItemModel;
import com.example.roommate.values.models.RoomBookingModel;
import com.example.roommate.values.models.RoomSearchModel;
import com.example.roommate.tests.controller.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ControllerRouteTest
class GetRoomsRouteTest extends ControllerHttpFixtureTest {
    @Test
    void guestCanSearchTheWorkspaceInventoryByTimeAndEquipment() throws Exception {
        String date = "2026-07-06";
        String startTime = "09:00";
        String endTime = "10:00";
        String requestedEquipment = "Monitor";
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        when(bookingApplicationService.getRoomSearchModel(eq(List.of(requestedEquipment)), any(SearchTimeForm.class), eq("__guest__")))
                .thenReturn(new RoomSearchModel(
                        date,
                        startTime,
                        endTime,
                        List.of(new ItemModel(requestedEquipment), new ItemModel("Dock")),
                        List.of(requestedEquipment),
                        List.of(new RoomBookingModel(ROOM_ID, WORKSPACE_ID, workspaceNumber, roomNumber, List.of(new ItemName(requestedEquipment))))
                ));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.searchesForWorkspaces(date, startTime, endTime, requestedEquipment)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains("Arbeitsplatzreservierung")
                                    && html.contains("Raum " + roomNumber)
                                    && html.contains("Workspace " + workspaceNumber)
                                    && html.contains(requestedEquipment));
        }, TIMEOUT, STEP);
    }
}
