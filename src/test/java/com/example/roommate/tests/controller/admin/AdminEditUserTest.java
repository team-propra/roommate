package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.models.UserModel;
import com.example.roommate.xcepto.controller.RoommateHttpClients;
import com.example.roommate.xcepto.controller.RunningRoommateScenario;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;
import org.xcepto.xceptoj.ssr.SsrXceptoAdapter;
import org.xcepto.xceptoj.ssr.builders.SsrAdapterBuilder;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class AdminEditUserTest extends ControllerHttpFixtureTest {
    @Test
    void adminCanOpenUserEditPage() throws Exception {
        String userName = "admin";
        when(adminApplicationService.getUserByHandle(userName))
                .thenReturn(new UserModel(userName, Set.of("ADMIN")));
        RunningRoommateScenario scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor(userName, "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.get("/admin/editUser/" + userName)
                    .withCustomName("Admin opens the user edit page")
                    .assertSuccess()
                    .assertThatResponseContentString(html -> html.contains("Berechtigungen verwalten"));
        });
    }

    @Test
    void adminCanRevokeAnotherUsersAdminRole() throws Exception {
        String userName = "other-admin";
        RunningRoommateScenario scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor("admin", "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.post("/admin/revokeAdmin/" + userName)
                    .withCustomName("Admin revokes another user's admin role")
                    .assertThatResponseStatus(302)
                    .assertThatResponse(response -> assertRedirectsTo(response, "/admin/editUser/" + userName));
        });

        verify(adminApplicationService).revokeAdmin(userName);
    }
}
