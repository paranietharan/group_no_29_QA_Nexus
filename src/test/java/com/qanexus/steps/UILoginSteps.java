package com.qanexus.steps;

import com.qanexus.config.EnvConfig;
import com.qanexus.driver.DriverFactory;
import com.qanexus.pages.DashboardPage;
import com.qanexus.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class UILoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private final EnvConfig config = EnvConfig.getInstance();

    private void initPages() {
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Given("the login page is displayed")
    public void theLoginPageIsDisplayed() {
        initPages();
        loginPage.navigateTo(config.getBaseUrl());
        assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
    }

    @When("I enter admin username {string}")
    public void iEnterAdminUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter admin password {string}")
    public void iEnterAdminPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("I enter user username {string}")
    public void iEnterUserUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("I enter user password {string}")
    public void iEnterUserPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("I leave username field empty")
    public void iLeaveUsernameFieldEmpty() {
        loginPage.enterUsername("");
    }

    @When("I leave password field empty")
    public void iLeavePasswordFieldEmpty() {
        loginPage.enterPassword("");
    }

    @When("I click the login button")
    public void iClickTheLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("I should be redirected to the dashboard")
    public void iShouldBeRedirectedToTheDashboard() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/ui/login")));
        } catch (Exception e) {
            // URL didn't change - test will fail with assertion below
        }
        String currentUrl = driver.getCurrentUrl();
        assertTrue(
            !currentUrl.contains("/ui/login"),
            "Should be redirected away from login page. Current URL: " + currentUrl
        );
    }

    @Then("the dashboard should be displayed")
    public void theDashboardShouldBeDisplayed() {
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed");
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }

    @Then("I should remain on the login page")
    public void iShouldRemainOnTheLoginPage() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("login"), "Should remain on login page. Current URL: " + currentUrl);
    }
}
