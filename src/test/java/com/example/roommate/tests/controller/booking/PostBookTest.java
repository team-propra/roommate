package com.example.roommate.tests.controller.booking;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.exceptions.applicationService.NotFoundException;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.forms.BookDataForm;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class PostBookTest extends ControllerHttpFixtureTest {
    @Test
    void missingWorkspaceInBookingFormIsRejectedAsBadRequest() throws Exception {
        int stepSize = 60;
        String selectedCell = "0-1-X";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.submitsBookingSelectionWithoutWorkspace(ROOM_ID, stepSize, selectedCell)
                    .assertThatResponseStatus(400);
        }, TIMEOUT, STEP);
    }

    @Test
    void invalidBookingSelectionKeepsTheBookerOnTheWorkspaceDetail() throws Exception {
        int stepSize = 60;
        String selectedCell = "0-1";
        when(bookingApplicationService.isBookingSelectionValid(any(BookDataForm.class), anyList())).thenReturn(false);
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.submitsBookingSelection(ROOM_ID, WORKSPACE_ID, stepSize, selectedCell)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/room/%s/workspace/%s".formatted(ROOM_ID, WORKSPACE_ID)));
        }, TIMEOUT, STEP);
    }

    @Test
    void verifiedBookerCanPersistAValidWorkspaceSelection() throws Exception {
        int stepSize = 60;
        String selectedCell = "0-1-X";
        String roomNumber = "A-12";
        int workspaceNumber = 4;
        String selectedEquipment = "Monitor";
        String availableEquipment = "Dock";
        when(bookingApplicationService.getWorkspaceDetailsModel(ROOM_ID, WORKSPACE_ID))
                .thenReturn(workspaceDetails(roomNumber, workspaceNumber, selectedEquipment, availableEquipment));
        when(bookingApplicationService.isBookingSelectionValid(any(BookDataForm.class), anyList())).thenReturn(true);
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.opensWorkspace(ROOM_ID, WORKSPACE_ID)
                    .assertSuccess()
                    .assertThatResponseContentString(html -> html.contains(selectedEquipment));

            roommate.submitsBookingSelection(ROOM_ID, WORKSPACE_ID, stepSize, selectedCell)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/"));
        }, TIMEOUT, STEP);

        ArgumentCaptor<List<String>> checkedDays = ArgumentCaptor.forClass(List.class);
        verify(bookingApplicationService).addBookEntry(any(BookDataForm.class), checkedDays.capture(), eq("verified"));
        assertThat(checkedDays.getValue()).containsExactly(selectedCell);
    }

    @Test
    void rejectedBookingSelectionIsReportedAsABadRequest() throws Exception {
        int stepSize = 60;
        String selectedCell = "0-1-X";
        when(bookingApplicationService.isBookingSelectionValid(any(BookDataForm.class), anyList())).thenReturn(true);
        doThrow(new NotFoundException()).when(bookingApplicationService)
                .addBookEntry(any(BookDataForm.class), anyList(), eq("verified"));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.submitsBookingSelection(ROOM_ID, WORKSPACE_ID, stepSize, selectedCell)
                    .assertThatResponseStatus(400);
        }, TIMEOUT, STEP);
    }
}
