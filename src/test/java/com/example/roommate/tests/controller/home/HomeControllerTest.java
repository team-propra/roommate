package com.example.roommate.tests.controller.home;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.Mockito.when;

@ControllerRouteTest
public class HomeControllerTest extends ControllerHttpFixtureTest {
    @Test
    void verifiedUserWithAKeyCanOpenTheDashboard() throws Exception {
        when(authenticationApplicationService.tryEnsureUserKey("verified")).thenReturn(true);
        when(bookingApplicationService.getRoomHomeModels()).thenReturn(List.of());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.verifiedBooker(builder, scenario.baseUri());

            roommate.opensPublicDashboard()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }

    @Test
    void guestCanOpenTheDashboardAsAPublicRoommateEntryPoint() throws Exception {
        when(bookingApplicationService.getRoomHomeModels()).thenReturn(List.of());
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensPublicDashboard()
                    .assertSuccess();
        }, TIMEOUT, STEP);
    }
}
