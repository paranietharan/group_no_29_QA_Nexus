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

// =========================================================
    // Common Step: Login (Used for ALL test cases)
    // =========================================================

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

 // =========================================================
    // TC_UI_SALES_USER_01 (Sales menu navigation)
    // TC_UI_SALES_USER_03 (Active menu highlight - uses menu click)
    // =========================================================

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

  // =========================================================
    // TC_UI_SALES_USER_02 (Direct URL access)
    // =========================================================

        @When("I access the Sales page via direct URL")
    public void iAccessTheSalesPageViaDirectURL() {
        WebDriver driver = DriverFactory.getDriver();
        driver.get(config.getUiBaseUrl() + "/ui/sales");
        salesPage = new SalesPage(driver);
    }

 // =========================================================
    // TC_UI_SALES_USER_03 (Active menu highlight)
    // =========================================================

@Then("Sales menu item should be highlighted as active")
public void salesMenuItemShouldBeHighlightedAsActive() {
    WebDriver driver = DriverFactory.getDriver();

    String classAttr = driver.findElement(org.openqa.selenium.By.linkText("Sales"))
            .getAttribute("class");

    assertNotNull(classAttr, "Sales menu should have a class attribute");
    assertTrue(classAttr.toLowerCase().contains("active"),
            "Sales menu item should be highlighted as active. Actual class: " + classAttr);
}

// =========================================================
    // TC_UI_SALES_USER_04 (No sales message)
    // =========================================================

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

// =========================================================
    // TC_UI_SALES_USER_05 (Default sorting by Sold Date desc)
    // =========================================================

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

   // =========================================================
    // TC_UI_SALES_USER_06 (Sell Plant button hidden for USER)
    // =========================================================

@Then("Sell Plant button should not be visible to USER")
public void sellPlantButtonShouldNotBeVisibleToUser() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    assertFalse(
            salesPage.isSellPlantButtonVisible(),
            "Sell Plant button should NOT be visible to USER"
    );
}

// =========================================================
    // TC_UI_SALES_USER_07 (Delete action hidden for USER)
    // =========================================================

@Then("Delete action should not be visible for USER in sales table")
public void deleteActionShouldNotBeVisibleForUserInSalesTable() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    // wait until table loads (or shows empty state)
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(d -> d.findElements(org.openqa.selenium.By.xpath("//table/tbody/tr")).size() >= 0);

    // Precondition: sales records exist (as per test case)
    assertTrue(salesPage.hasSalesRecords(),
            "Precondition failed: Sales records should exist to validate Actions column (TC_UI_SALES_USER_07)");

    // Main validation: Delete action should NOT be visible for USER
    assertFalse(salesPage.isDeleteActionVisible(),
            "Delete action should NOT be visible for USER in Actions column");
}

// =========================================================
    // TC_UI_SALES_USER_08 (Block access to Sell Plant page by URL)
    // =========================================================

@When("I try to access Sell Plant page via direct URL")
public void iTryToAccessSellPlantPageViaDirectURL() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    salesPage.navigateToSellPlantPage(config.getUiBaseUrl());
}

@Then("User should be redirected to Access Denied page")
public void userShouldBeRedirectedToAccessDeniedPage() {
    WebDriver driver = DriverFactory.getDriver();

    // Wait for redirect or page load
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(d -> salesPage.isAccessDeniedDisplayed()
                    || driver.getCurrentUrl().contains("403"));

    // Validate access denied message OR 403 page
    boolean accessDeniedVisible = salesPage.isAccessDeniedDisplayed();
    boolean urlContains403 = driver.getCurrentUrl().contains("403");

    assertTrue(accessDeniedVisible || urlContains403,
            "User should be redirected to Access Denied / 403 page");
}

// =========================================================
    // TC_UI_SALES_USER_09 (Pagination is present)
    // =========================================================

@Then("Pagination controls should be visible and USER can navigate pages")
public void paginationControlsShouldBeVisibleAndUserCanNavigatePages() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    // Scroll to bottom (as per test case)
    salesPage.scrollToBottom();

    // Validate pagination visibility
    assertTrue(salesPage.isPaginationVisible(),
            "Pagination controls should be displayed when records exceed one page");

    // Verify USER can navigate: click Next and confirm table changes
    String before = salesPage.getFirstRowFirstCellText();

    assertTrue(salesPage.isNextEnabled(),
            "Next button should be enabled so USER can navigate pages");

    salesPage.clickNext();

    // Wait until first row changes (page navigated)
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(d -> {
                try {
                    String after = salesPage.getFirstRowFirstCellText();
                    return after != null && !after.equals(before);
                } catch (Exception e) {
                    return false;
                }
            });

    String after = salesPage.getFirstRowFirstCellText();
    assertNotEquals(before, after,
            "USER navigation failed: first row did not change after clicking Next");
}

 // =========================================================
    // TC_UI_SALES_ADMIN_01 (Sell Plant visible + enabled)
    // =========================================================

@Then("Sell Plant button should be visible and enabled for ADMIN")
public void sellPlantButtonShouldBeVisibleAndEnabledForAdmin() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    assertTrue(salesPage.isSellPlantButtonVisible(),
            "Sell Plant button should be visible for ADMIN");

    assertTrue(salesPage.isSellPlantButtonEnabled(),
            "Sell Plant button should be enabled for ADMIN (clickable)");

    // Optional: click to confirm it is clickable (no exception should happen)
    assertDoesNotThrow(() -> salesPage.clickSellPlantButton(),
            "Sell Plant button should be clickable for ADMIN");
}

// =========================================================
    // TC_UI_SALES_ADMIN_02 (Open Sell Plant form)
    // =========================================================

@When("I click the Sell Plant button")
public void iClickTheSellPlantButton() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    salesPage.clickSellPlantButton();
}
@Then("Sell Plant form should open with plant selection and quantity fields")
public void sellPlantFormShouldOpenWithFields() {

    WebDriver driver = DriverFactory.getDriver();

    // Wait for form to appear
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(d -> salesPage.isSellPlantFormVisible());

    assertTrue(salesPage.isSellPlantFormVisible(),
            "Sell Plant form should be displayed");

    assertTrue(salesPage.isPlantDropdownVisible(),
            "Plant selection dropdown should be visible");

    assertTrue(salesPage.isQuantityInputVisible(),
            "Quantity input field should be visible");
}

// =========================================================
    // TC_UI_SALES_ADMIN_03 (Quantity > stock validation)
    // =========================================================

@When("I select a plant with available stock")
public void iSelectAPlantWithAvailableStock() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    // wait dropdown visible
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(d -> salesPage.isPlantDropdownVisible());

    salesPage.selectFirstPlantOption();
}
@When("I enter quantity greater than available stock")
public void iEnterQuantityGreaterThanAvailableStock() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(d -> salesPage.isQuantityInputVisible());

    int stock = salesPage.getAvailableStockFromDropdownOption();

    // If stock is not shown in dropdown label, use a huge value to exceed any stock
    int invalidQty = (stock > 0) ? stock + 1 : 9999;

    salesPage.enterQuantity(String.valueOf(invalidQty));
}
@Then("Validation error should be displayed for quantity greater than stock")
public void validationErrorShouldBeDisplayedForQuantityGreaterThanStock() {
    WebDriver driver = DriverFactory.getDriver();
    salesPage = new SalesPage(driver);

    // Give UI a moment to validate
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
            .until(d -> {
                String msg = salesPage.getHtml5QuantityValidationMessage();
                return (msg != null && !msg.trim().isEmpty())
                        || salesPage.isQuantityErrorVisible()
                        || salesPage.isSubmitDisabledOrNotClickable();
            });

    String html5Msg = salesPage.getHtml5QuantityValidationMessage();
    boolean inlineError = salesPage.isQuantityErrorVisible();
    boolean submitBlocked = salesPage.isSubmitDisabledOrNotClickable();

    assertTrue(
            (html5Msg != null && !html5Msg.trim().isEmpty()) || inlineError || submitBlocked,
            "Expected validation for quantity > stock, but none found. " +
                    "HTML5 msg: " + html5Msg + ", inlineError: " + inlineError + ", submitBlocked: " + submitBlocked
    );
}


}