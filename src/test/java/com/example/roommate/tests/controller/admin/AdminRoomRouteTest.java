package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.ControllerHttpFixtureTest;
import com.example.roommate.values.models.RoomOverviewModel;
import com.example.roommate.values.models.WorkspaceOverviewModel;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerRouteTest
class AdminRoomRouteTest extends ControllerHttpFixtureTest {
    @Test
    void adminCanOpenRoomInventoryForWorkspaceManagement() throws Exception {
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        String itemName = "Monitor";
        when(bookingApplicationService.getRoomOverviewModel(ROOM_ID))
                .thenReturn(new RoomOverviewModel(
                        ROOM_ID,
                        roomNumber,
                        List.of(new WorkspaceOverviewModel(WORKSPACE_ID, workspaceNumber, List.of(itemName), List.of("Monday[09:00 - 10:00]")))
                ));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensRoomInventory(ROOM_ID)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains("Raum <span>" + roomNumber + "</span>")
                                    && html.contains("Arbeitsplätze")
                                    && html.contains(">" + workspaceNumber + "</a>")
                                    && html.contains(itemName));
        }, TIMEOUT, STEP);
    }

    @Test
    void adminCanCreateAWorkspaceInsideARoomInventory() throws Exception {
        String workspaceNumber = "17";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.createsWorkspace(ROOM_ID, workspaceNumber)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/room/" + ROOM_ID));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).addWorkspace(workspaceNumber, ROOM_ID);
    }

    @Test
    void adminCanRemoveAWorkspaceFromARoomInventory() throws Exception {
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.deletesWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/room/" + ROOM_ID));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).removeWorkspace(WORKSPACE_ID, ROOM_ID);
    }

    @Test
    void adminCanAddEquipmentToAWorkspaceAndReturnToWorkspaceDetail() throws Exception {
        String itemName = "Dock";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.addsWorkspaceItem(ROOM_ID, WORKSPACE_ID, itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/room/%s/workspace/%s".formatted(ROOM_ID, WORKSPACE_ID)));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).addItemToRoom(WORKSPACE_ID, itemName, ROOM_ID);
    }

    @Test
    void adminCanRemoveEquipmentFromAWorkspaceAndReturnToWorkspaceDetail() throws Exception {
        String itemName = "Dock";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.removesWorkspaceItem(ROOM_ID, WORKSPACE_ID, itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/room/%s/workspace/%s".formatted(ROOM_ID, WORKSPACE_ID)));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).removeItemFromRoom(WORKSPACE_ID, itemName, ROOM_ID);
    }
}
