package com.qanexus.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class ApiHooks {

    @Before("@api")
    public void setUp(Scenario scenario) {
        // Setup for API tests
    }

    @After("@api")
    public void tearDown(Scenario scenario) {
        // Cleanup after API tests
    }
}
