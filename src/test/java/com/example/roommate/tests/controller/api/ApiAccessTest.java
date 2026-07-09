package com.example.roommate.tests.controller.api;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.forms.KeyMasterForm;
import com.example.roommate.xcepto.controller.RoommateHttp;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;

import java.util.List;

import static org.mockito.Mockito.when;

@ControllerRouteTest
public class ApiAccessTest extends ControllerHttpFixtureTest {
    @Test
    void keymasterCanReadTheCurrentWorkspaceAccessRegistry() throws Exception {
        when(bookingApplicationService.getAssociatedBookEntries())
                .thenReturn(List.of(new KeyMasterForm(WORKSPACE_ID, KEY_ID)));
        var scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            var roommate = RoommateHttp.admin(builder, scenario.baseUri());

            roommate.opensKeymasterAccessRegistry()
                    .assertSuccess()
                    .assertThatResponseContentString(json ->
                            json.contains(WORKSPACE_ID.toString()) && json.contains(KEY_ID.toString()));
        }, TIMEOUT, STEP);
    }
}
