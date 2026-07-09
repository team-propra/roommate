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
public class AdminEditUserTest extends ControllerHttpFixtureTest {
    @Test
    public void adminEditUserIsReachable() throws XceptoAdapterTerminationException, XceptoAdapterInitializationException, XceptoTestFailedException, XceptoScenarioResetException {
        RunningRoommateScenario scenario = roommateIsRunning();
        String userName = "admin";
        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor(userName, "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.get("/admin/editUser")
                    .withCustomName("/admin/editUsers check")
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                    {
                        System.out.println(html);
                        return html.contains("Users Berechtigung geben/entziehen");
                    }) ;
            
            browser.get("/admin/editUser/")
                    .withCustomName("/admin/editUsers/ check")
                    .assertSuccess()
                    .assertThatResponseContentString(html ->
                            html.contains("Users Berechtigung geben/entziehen"));
        });

    }

    @Test
    public void adminEditUserWorks() throws XceptoAdapterTerminationException, XceptoAdapterInitializationException, XceptoTestFailedException, XceptoScenarioResetException {
        RunningRoommateScenario scenario = roommateIsRunning();
        String userName = "admin";
        Xcepto.given(scenario, builder -> {
            SsrXceptoAdapter browser = new SsrAdapterBuilder(builder)
                    .withHttpClient(RoommateHttpClients.browserFor(userName, "admin"))
                    .withBaseUrl(scenario.baseUri())
                    .build();

            browser.post("/admin/editUser")
                    .withCustomName("empty edit change")
                    .assertSuccess();
        });

    }
}
