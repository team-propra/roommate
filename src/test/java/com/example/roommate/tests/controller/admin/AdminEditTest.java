package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.models.AdminEditModel;
import com.example.roommate.values.models.AdminRoomModel;
import com.example.roommate.values.models.ItemModel;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class AdminEditTest extends ControllerHttpFixtureTest {
    @Test
    void adminCanOpenTheRoommateBackofficeCatalog() throws Exception {
        String roomNumber = "A-12";
        String itemName = "Monitor";
        when(bookingApplicationService.getAdminEditModel())
                .thenReturn(new AdminEditModel(
                        List.of(new ItemModel(itemName)),
                        List.of(new AdminRoomModel(ROOM_ID, roomNumber))
                ));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensAdminCenter()
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(roomNumber)
                                    && html.contains(itemName));
        }, TIMEOUT, STEP);
    }

    @Test
    void adminCanCreateAnEquipmentCatalogItemAndReturnToTheBackoffice() throws Exception {
        String itemName = "Whiteboard";
        when(bookingApplicationService.getAdminEditModel())
                .thenReturn(new AdminEditModel(List.of(), List.of()));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensAdminCenter()
                    .assertSuccess();

            roommate.createsCatalogItem(itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/edit"));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).createItem(itemName);
    }

    @Test
    void adminCanRemoveAnEquipmentCatalogItemAndReturnToTheBackoffice() throws Exception {
        String itemName = "Whiteboard";
        when(bookingApplicationService.getAdminEditModel())
                .thenReturn(new AdminEditModel(List.of(new ItemModel(itemName)), List.of()));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensAdminCenter()
                    .assertSuccess()
                    .assertThatResponseContentString(html -> html.contains(itemName));

            roommate.deletesCatalogItem(itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/edit"));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).removeItem(itemName);
    }
}
