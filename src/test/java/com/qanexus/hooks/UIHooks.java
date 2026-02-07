package com.qanexus.hooks;

import com.qanexus.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class UIHooks {

    @Before("@ui")
    public void setUp() {
        DriverFactory.getDriver();
    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed() && driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "failure-screenshot");
        }
            if (scenario.isFailed() && driver != null) {
                // Capture page HTML for debugging
                try {
                    String pageSource = driver.getPageSource();
                    scenario.attach(pageSource.getBytes(), "text/html", "failure-page-source.html");
                    scenario.log("Current URL: " + driver.getCurrentUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        DriverFactory.quitDriver();
    }
}
