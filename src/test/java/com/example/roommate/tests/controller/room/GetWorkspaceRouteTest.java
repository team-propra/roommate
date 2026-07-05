package com.example.roommate.tests.controller.room;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.tests.controller.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import static org.mockito.Mockito.when;

@ControllerRouteTest
class GetWorkspaceRouteTest extends ControllerHttpFixtureTest {
    @Test
    void guestCanInspectAWorkspaceButOnlyVerifiedUsersCanBookIt() throws Exception {
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        String selectedEquipment = "Monitor";
        String availableEquipment = "Dock";
        String expectedGuestRestriction = "Als Gast kannst du diesen Arbeitsplatz ansehen";
        when(bookingApplicationService.getWorkspaceDetailsModel(ROOM_ID, WORKSPACE_ID))
                .thenReturn(workspaceDetails(roomNumber, workspaceNumber, selectedEquipment, availableEquipment));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains("Workspace Detail")
                                    && html.contains(roomNumber)
                                    && html.contains(String.valueOf(workspaceNumber))
                                    && html.contains(selectedEquipment)
                                    && html.contains(expectedGuestRestriction));
        }, TIMEOUT, STEP);
    }

    @Test
    void missingWorkspaceIsReportedAsANotFoundBookingResource() throws Exception {
        String expectedTitle = "Nicht gefunden";
        String expectedMessage = "Die angeforderte Ressource konnte nicht gefunden werden.";
        when(bookingApplicationService.getWorkspaceDetailsModel(ROOM_ID, WORKSPACE_ID))
                .thenThrow(new NotFoundException());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertThatResponseStatus(404)
                    .assertThatResponseContentString(html ->
                            html.contains(expectedTitle) && html.contains(expectedMessage));
        }, TIMEOUT, STEP);
    }
}
