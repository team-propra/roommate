package com.example.roommate.tests.controller.resources;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

@ControllerRouteTest
public class StaticResourceTest extends ControllerHttpFixtureTest {
    @Test
    void stylesheetIsServedForBrowserRequests() throws Exception {
        String expectedNavigationClass = ".app-nav";
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.anonymousGuest(builder, scenario.baseUri());

            roommate.opensStylesheet()
                    .assertSuccess()
                    .assertThatResponseContentString(css -> css.contains(expectedNavigationClass));
        }, TIMEOUT, STEP);
    }
}
