package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final String LOGIN_URL = "/ui/login";

    private By usernameLocator = By.cssSelector("input[name='username'], input[id='username'], input[type='text']");
    private By passwordLocator = By.cssSelector("input[name='password'], input[id='password'], input[type='password']");
    private By loginButtonLocator = By.cssSelector("button[type='submit'], input[type='submit'], .login-btn, #loginBtn, button");
    private By errorMessageLocator = By.cssSelector(".error-message, .alert-danger, .error, [role='alert'], .invalid-feedback, .text-danger");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + LOGIN_URL);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(usernameLocator),
            ExpectedConditions.presenceOfElementLocated(passwordLocator)
        ));
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(usernameLocator));
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(passwordLocator));
        field.clear();
        field.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        btn.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginPageDisplayed() {
        try {
            return driver.findElement(usernameLocator).isDisplayed()
                && driver.findElement(passwordLocator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

