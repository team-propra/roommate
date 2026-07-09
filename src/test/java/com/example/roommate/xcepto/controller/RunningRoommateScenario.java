package com.example.roommate.xcepto.controller;

import org.xcepto.xceptoj.Scenario;
import org.xcepto.xceptoj.exceptions.XceptoScenarioResetException;

import java.net.URI;

public class RunningRoommateScenario extends Scenario {
    private final URI baseUri;

    public RunningRoommateScenario(int port) {
        this.baseUri = URI.create("http://localhost:" + port);
    }

    public URI baseUri() {
        return baseUri;
    }

    @Override
    public void stopEnvironment() throws XceptoScenarioResetException {
    }
}
