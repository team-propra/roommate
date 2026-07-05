package com.example.roommate.tests.controller.home;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.Mockito.when;

@ControllerRouteTest
class HomeControllerRouteTest extends ControllerHttpFixtureTest {
    @Test
    void guestCanOpenTheDashboardAsAPublicRoommateEntryPoint() throws Exception {
        String expectedHeading = "Willkommen bei Roommate";
        String expectedAccessMode = "Gastzugang";
        String expectedRoomBrowseAction = "Als Gast fortfahren";
        when(bookingApplicationService.getRoomHomeModels()).thenReturn(List.of());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensPublicDashboard()
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(expectedHeading)
                                    && html.contains(expectedAccessMode)
                                    && html.contains(expectedRoomBrowseAction));
        }, TIMEOUT, STEP);
    }
}
