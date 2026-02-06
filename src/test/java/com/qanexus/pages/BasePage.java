package com.qanexus.pages;

import com.qanexus.config.EnvConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final EnvConfig config = EnvConfig.getInstance();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeout()));
        PageFactory.initElements(driver, this);
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void click(WebElement element) {
        waitForClickable(element);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (Exception ignored) {
        }

        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // try waiting for common overlays to disappear then JS-click as a fallback
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop, .toast, .spinner, .loading")));
            } catch (Exception ignored) {
            }
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                throw e;
            }
        }
    }

    protected void type(WebElement element, String text) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        waitForVisibility(element);
        return element.getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
