package com.example.roommate.tests.controller;

import com.example.roommate.annotations.TestClass;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.application.services.AuthenticationApplicationService;
import com.example.roommate.application.services.BookingApplicationService;
import com.example.roommate.application.services.KeyMasterApplicationService;
import com.example.roommate.values.models.BookingFrameModel;
import com.example.roommate.values.models.WorkspaceDetailsModel;
import com.example.roommate.xcepto.controller.RunningRoommateScenario;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.xcepto.xceptoj.TimeoutConfig;
import org.xcepto.xceptoj.exceptions.XceptoTestFailedException;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@TestClass
public abstract class ControllerHttpFixtureTest {
    protected static final TimeoutConfig TIMEOUT = new TimeoutConfig(Duration.ofSeconds(15), Duration.ofSeconds(5));
    protected static final Duration STEP = Duration.ofMillis(100);

    protected static final UUID ROOM_ID = UUID.fromString("9e255449-449b-4564-8bc0-5e4517708364");
    protected static final UUID WORKSPACE_ID = UUID.fromString("30d2d8d5-ce75-499f-b1fe-904b5f55d2f0");
    protected static final UUID KEY_ID = UUID.fromString("77f11d9e-6894-402e-8b44-8e096fe91d39");

    @LocalServerPort
    protected int port;

    @MockBean
    protected BookingApplicationService bookingApplicationService;

    @MockBean
    protected AuthenticationApplicationService authenticationApplicationService;

    @MockBean
    protected AdminApplicationService adminApplicationService;

    @MockBean
    protected KeyMasterApplicationService keyMasterApplicationService;

    protected RunningRoommateScenario roommateIsRunning() {
        return new RunningRoommateScenario(port);
    }

    protected static WorkspaceDetailsModel workspaceDetails(String roomNumber, int workspaceNumber, String selectedItem, String availableItem) {
        return new WorkspaceDetailsModel(
                ROOM_ID,
                roomNumber,
                WORKSPACE_ID,
                workspaceNumber,
                List.of(selectedItem),
                List.of(availableItem),
                new BookingFrameModel(
                        60,
                        7,
                        1,
                        List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
                        List.of("09:00 - 10:00"),
                        List.of(
                                List.of(false),
                                List.of(false),
                                List.of(false),
                                List.of(false),
                                List.of(false),
                                List.of(false),
                                List.of(false)
                        )
                )
        );
    }

    protected static void assertRedirectsTo(HttpResponse<String> response, String expectedPath) throws XceptoTestFailedException {
        String location = response.headers().firstValue("location")
                .orElseThrow(() -> new XceptoTestFailedException("Expected redirect location header"));
        if (!location.endsWith(expectedPath)) {
            throw new XceptoTestFailedException("Expected redirect to end with %s but was %s".formatted(expectedPath, location));
        }
    }
}
