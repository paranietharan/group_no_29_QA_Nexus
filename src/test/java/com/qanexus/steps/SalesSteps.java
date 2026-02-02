package com.qanexus.steps;

import com.qanexus.pages.SalesPage;
import com.qanexus.driver.DriverFactory; // <-- change if your class name differs
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class SalesSteps {

    private SalesPage salesPage;

    @When("I click the Sales menu from sidebar")
    public void iClickTheSalesMenuFromSidebar() {
        WebDriver driver = DriverFactory.getDriver(); // <-- change based on your framework
        salesPage = new SalesPage(driver);
        salesPage.clickSalesMenu();
    }

    @Then("Sales page should load and URL should contain {string}")
    public void salesPageShouldLoadAndUrlShouldContain(String expectedPath) {
        String url = salesPage.getCurrentUrl();
        assertTrue(url.contains(expectedPath),
                "Expected URL to contain " + expectedPath + " but got: " + url);
    }
}

