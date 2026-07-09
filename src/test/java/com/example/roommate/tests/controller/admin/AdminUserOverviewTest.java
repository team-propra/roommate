package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.tests.controller.fixture.ControllerHttpFixtureTest;
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

@ControllerRouteTest
public class AdminUserOverviewTest extends ControllerHttpFixtureTest {
    @Test
    public void adminUsersIsReachable() throws XceptoAdapterTerminationException, XceptoAdapterInitializationException, XceptoTestFailedException, XceptoScenarioResetException {
        RunningRoommateScenario scenario = roommateIsRunning();
        String userName = "admin";
        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor(userName, "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.get("/admin/users")
                    .withCustomName("/admin/users check")
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                    {
                        System.out.println(html);
                        return html.contains("User: " + "F3lix.Lo3h");
                    }) ;
            
            browser.get("/admin/users/")
                    .withCustomName("/admin/users/ check")
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains("User: " + "F3lix.Lo3h"));
        });

    }
}
