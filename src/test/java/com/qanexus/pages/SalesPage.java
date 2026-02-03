package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SalesPage {

    private final WebDriver driver;

    // Sidebar Sales menu
    private final By salesMenu = By.linkText("Sales");
    // "No sales found" message (adjust if your UI text differs)
    private final By noSalesFoundMessage = By.xpath("//*[contains(normalize-space(),'No sales found')]");

    // Sales table header row / headers (generic – update if your UI uses different structure)
    private final By salesTableHeaders = By.cssSelector("table thead tr");

    public SalesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickSalesMenu() {
        driver.findElement(salesMenu).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isSalesMenuHighlighted() {
        String classAttribute = driver.findElement(salesMenu).getAttribute("class");
        return classAttribute != null && classAttribute.toLowerCase().contains("active");
    }

        public boolean isNoSalesFoundMessageVisible() {
        return !driver.findElements(noSalesFoundMessage).isEmpty()
                && driver.findElement(noSalesFoundMessage).isDisplayed();
    }

    public boolean areSalesTableHeadersVisible() {
        return !driver.findElements(salesTableHeaders).isEmpty()
                && driver.findElement(salesTableHeaders).isDisplayed();
    }

}
