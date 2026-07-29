package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class GetRoomTest extends ControllerHttpFixtureTest {
    @Test
    void guestCanInspectAWorkspaceButOnlyVerifiedUsersCanBookIt() throws Exception {
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        String selectedEquipment = "Monitor";
        String availableEquipment = "Dock";
        when(bookingApplicationService.getWorkspaceDetailsModel(eq(ROOM_ID), eq(WORKSPACE_ID), any(Locale.class)))
                .thenReturn(workspaceDetails(roomNumber, workspaceNumber, selectedEquipment, availableEquipment));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(roomNumber)
                                    && html.contains(String.valueOf(workspaceNumber))
                                    && html.contains(selectedEquipment));
        }, TIMEOUT, STEP);
    }

    @Test
    void missingWorkspaceIsReportedAsANotFoundBookingResource() throws Exception {
        when(bookingApplicationService.getWorkspaceDetailsModel(eq(ROOM_ID), eq(WORKSPACE_ID), any(Locale.class)))
                .thenThrow(new NotFoundException());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertThatResponseStatus(404);
        }, TIMEOUT, STEP);
    }
}
