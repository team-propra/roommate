package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.application.services.AdminApplicationService;
import com.example.roommate.domain.models.entities.User;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
import com.example.roommate.values.forms.SearchTimeForm;
import com.example.roommate.values.models.RoomSearchModel;
import com.example.roommate.values.models.UserModel;
import com.example.roommate.values.models.UsersModel;
import com.example.roommate.xcepto.controller.RoommateHttpClients;
import com.example.roommate.xcepto.controller.RunningRoommateScenario;
import org.junit.jupiter.api.Test;
import org.xcepto.xceptoj.Xcepto;
import org.xcepto.xceptoj.exceptions.XceptoAdapterInitializationException;
import org.xcepto.xceptoj.exceptions.XceptoAdapterTerminationException;
import org.xcepto.xceptoj.exceptions.XceptoScenarioResetException;
import org.xcepto.xceptoj.exceptions.XceptoTestFailedException;
import org.xcepto.xceptoj.ssr.SsrXceptoAdapter;
import org.xcepto.xceptoj.ssr.builders.SsrAdapterBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ControllerRouteTest
public class AdminUserOverviewTest extends ControllerHttpFixtureTest {
    @Test
    public void adminUsersIsReachable() throws XceptoAdapterTerminationException, XceptoAdapterInitializationException, XceptoTestFailedException, XceptoScenarioResetException {
        UserModel user1 = new UserModel("Peter", new HashSet<>(List.of("ROLE_ADMIN")));
        UserModel user2 = new UserModel("Manfred", new HashSet<>(List.of("ROLE_VERIFIED_USER")));
        UserModel user3 = new UserModel("Günther", new HashSet<>(List.of("ROLE_ADMIN")));
        User admin = new User(UUID.randomUUID(),"admin","ADMIN");

        UsersModel users = new UsersModel(List.of(user1,user2,user3));

        when(adminApplicationService.getUsers())
                .thenReturn(users);
        when(authenticationApplicationService.getUserByLogin(admin.getHandle()))
                .thenReturn(admin);

        RunningRoommateScenario scenario = roommateIsRunning();

        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor(admin.getHandle(), "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.get("/admin/users")
                    .withCustomName("/admin/users check")
                    .withRetry(false)
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(user1.userName()) && html.contains(user2.userName()));
            
            browser.get("/admin/users/")
                    .withCustomName("/admin/users/ check")
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains(user1.userName()) && html.contains(user2.userName()));
        });

    }
}
