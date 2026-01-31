package com.qanexus.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    @FindBy(css = ".dashboard, .main-content, #dashboard, .home")
    private WebElement dashboardContent;

    @FindBy(css = ".logout-btn, #logout, a[href*='logout']")
    private WebElement logoutButton;

    @FindBy(css = ".user-name, .username, .welcome-message")
    private WebElement welcomeMessage;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardDisplayed() {
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("home"),
                ExpectedConditions.visibilityOf(dashboardContent)
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLogoutButtonVisible() {
        try {
            waitForVisibility(logoutButton);
            return logoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogout() {
        click(logoutButton);
    }

    public String getWelcomeMessage() {
        try {
            waitForVisibility(welcomeMessage);
            return welcomeMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
