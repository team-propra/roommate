package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.ControllerHttpFixtureTest;
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
class AdminEditRouteTest extends ControllerHttpFixtureTest {
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
                            html.contains("Admin Center")
                                    && html.contains(roomNumber)
                                    && html.contains(itemName));
        }, TIMEOUT, STEP);
    }

    @Test
    void adminCanCreateAnEquipmentCatalogItemAndReturnToTheBackoffice() throws Exception {
        String itemName = "Whiteboard";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.createsCatalogItem(itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/edit"));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).createItem(itemName);
    }

    @Test
    void adminCanRemoveAnEquipmentCatalogItemAndReturnToTheBackoffice() throws Exception {
        String itemName = "Whiteboard";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.deletesCatalogItem(itemName)
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/edit"));
        }, TIMEOUT, STEP);

        verify(bookingApplicationService).removeItem(itemName);
    }
}
