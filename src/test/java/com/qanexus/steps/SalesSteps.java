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
import org.openqa.selenium.WebElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

        @When("I access the Sales page via direct URL")
    public void iAccessTheSalesPageViaDirectURL() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get(config.getUiBaseUrl() + "/ui/sales");
        salesPage = new SalesPage(driver);
    }

       //testcase 4

@Then("Sales menu item should be highlighted as active")
public void salesMenuItemShouldBeHighlightedAsActive() {
    WebDriver driver = DriverFactory.getDriver();

    String classAttr = driver.findElement(org.openqa.selenium.By.linkText("Sales"))
            .getAttribute("class");

    assertNotNull(classAttr, "Sales menu should have a class attribute");
    assertTrue(classAttr.toLowerCase().contains("active"),
            "Sales menu item should be highlighted as active. Actual class: " + classAttr);
}

//testcase 5

   @Then("No sales found message should be displayed and sales table headers should be visible")
public void noSalesFoundMessageShouldBeDisplayedAndHeadersVisible() {
    WebDriver driver = DriverFactory.getDriver();

    // Check "No sales found" message
    String noSalesText = driver
            .findElement(org.openqa.selenium.By.xpath("//*[contains(normalize-space(),'No sales found')]"))
            .getText();

    assertNotNull(noSalesText, "\"No sales found\" message element should exist");
    assertTrue(noSalesText.toLowerCase().contains("no sales"),
            "\"No sales found\" message should be displayed");

    // Check sales table headers are visible
    boolean headersVisible = driver
            .findElements(org.openqa.selenium.By.cssSelector("table thead tr"))
            .size() > 0;

    assertTrue(headersVisible,
            "Sales table headers should remain visible even when no sales exist");
}

@Then("Sales records should be sorted by Sold Date in descending order")
public void salesRecordsShouldBeSortedBySoldDateInDescendingOrder() {
    WebDriver driver = DriverFactory.getDriver();

    // Wait a moment for the table to load
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(d -> d.findElements(org.openqa.selenium.By.xpath("//table/tbody/tr")).size() > 0);

    // ✅ Sold At is the 4th column in your UI (see screenshot)
    List<WebElement> soldDateElements = driver.findElements(
            org.openqa.selenium.By.xpath("//table/tbody/tr/td[4]")
    );

    assertTrue(soldDateElements.size() >= 2,
            "At least two sales records should exist to verify sorting");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    List<LocalDateTime> soldDates = new ArrayList<>();

    for (WebElement element : soldDateElements) {
        String dateText = element.getText().trim();
        assertFalse(dateText.isEmpty(), "Sold At should not be empty");
        soldDates.add(LocalDateTime.parse(dateText, formatter));
    }

    // Verify descending: each row date >= next row date
    for (int i = 0; i < soldDates.size() - 1; i++) {
        assertTrue(
                soldDates.get(i).isAfter(soldDates.get(i + 1)) ||
                        soldDates.get(i).isEqual(soldDates.get(i + 1)),
                "Sales are not sorted by Sold At descending. Row " + (i + 1) + " = " + soldDates.get(i)
                        + ", Row " + (i + 2) + " = " + soldDates.get(i + 1)
        );
    }
}



}