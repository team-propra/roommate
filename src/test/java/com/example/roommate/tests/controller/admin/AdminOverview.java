package com.example.roommate.tests.controller.admin;

import com.example.roommate.annotations.ControllerRouteTest;
import com.example.roommate.annotations.TestClass;
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
import org.xcepto.xceptoj.ssr.builders.SsrStateBuilderIdentity;

@ControllerRouteTest
public class AdminOverview extends ControllerHttpFixtureTest {
    @Test
    public void adminOverviewIsReachable() throws XceptoAdapterTerminationException, XceptoAdapterInitializationException, XceptoTestFailedException, XceptoScenarioResetException {
        RunningRoommateScenario scenario = roommateIsRunning();
        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor("admin", "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();
            browser.get("/admin")
                    .withCustomName("reachableCheck")
                    .assertSuccess();
            browser.get("/admin")
                    .withCustomName("html check")
                    .assertThatResponseContentString(html ->
                            html.contains("User Overview"));
        });

    }
}
