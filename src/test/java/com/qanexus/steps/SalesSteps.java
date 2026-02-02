package com.qanexus.steps;

import com.qanexus.config.EnvConfig;
import com.qanexus.pages.DashboardPage;
import com.qanexus.pages.LoginPage;
import com.qanexus.pages.SalesPage;
import com.qanexus.driver.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class SalesSteps {

    private SalesPage salesPage;
    private final EnvConfig config = EnvConfig.getInstance();

    @Given("I login to the application as {string}")
    public void iLoginToTheApplicationAs(String role) {
        WebDriver driver = DriverFactory.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        String username;
        String password;
        if ("ADMIN".equalsIgnoreCase(role)) {
            username = config.get("ui.admin.username", "admin");
            password = config.get("ui.admin.password", "admin123");
        } else {
            username = config.get("ui.user.username", "testuser");
            password = config.get("ui.user.password", "test123");
        }

        loginPage.navigateTo(config.getUiBaseUrl());
        loginPage.login(username, password);
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after login");
    }

    @When("I click the Sales menu from sidebar")
    public void iClickTheSalesMenuFromSidebar() {
        WebDriver driver = DriverFactory.getDriver();
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
