package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SalesPage {

    private final WebDriver driver;

    // Sidebar Sales menu (change this locator if your UI is different)
    private final By salesMenu = By.linkText("Sales");

    public SalesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickSalesMenu() {
        driver.findElement(salesMenu).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
