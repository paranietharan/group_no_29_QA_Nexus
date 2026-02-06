package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final String loginPath = config.getUiLoginPath();

    private By usernameLocator = By.cssSelector("input[name='username'], input[id='username'], input[type='text']");
    private By passwordLocator = By.cssSelector("input[name='password'], input[id='password'], input[type='password']");
    private By loginButtonLocator = By.cssSelector("button[type='submit'], input[type='submit'], .login-btn, #loginBtn, button");
    private By errorMessageLocator = By.cssSelector(".error-message, .alert-danger, .error, [role='alert'], .text-danger");
    private By validationFeedbackLocator = By.cssSelector(".invalid-feedback");
    private By invalidInputLocator = By.cssSelector(".form-control.is-invalid");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + loginPath);
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

    public boolean isPasswordMasked() {
        try {
            WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(passwordLocator));
            String type = field.getAttribute("type");
            return type != null && type.equalsIgnoreCase("password");
        } catch (Exception e) {
            return false;
        }
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
            // First check for err msg
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return error.isDisplayed();
        } catch (Exception e) {
            // Check for validation feedback
            return isValidationMessageDisplayed();
        }
    }

    public boolean isValidationMessageDisplayed() {
        try {
            // Check if any input has is-invalid class
            java.util.List<WebElement> invalidInputs = driver.findElements(invalidInputLocator);
            if (!invalidInputs.isEmpty()) {
                return true;
            }
            // Check for invalid-feedback divs with no empty text
            java.util.List<WebElement> feedbacks = driver.findElements(validationFeedbackLocator);
            for (WebElement feedback : feedbacks) {
                String text = feedback.getText();
                if (text != null && !text.trim().isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return error.getText();
        } catch (Exception e) {
            return getValidationMessageText();
        }
    }

    public String getValidationMessageText() {
        try {
            java.util.List<WebElement> feedbacks = driver.findElements(validationFeedbackLocator);
            for (WebElement feedback : feedbacks) {
                String text = feedback.getText();
                if (text != null && !text.trim().isEmpty()) {
                    return text;
                }
            }
            return "";
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

