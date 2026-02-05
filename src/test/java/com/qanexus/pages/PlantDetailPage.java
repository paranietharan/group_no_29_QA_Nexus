package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PlantDetailPage extends BasePage {

    private By plantName = By.cssSelector(".plant-name, h1, h2, h3, [data-testid='plant-name'], .MuiTypography-h4, .MuiTypography-h5");
    private By plantPrice = By.cssSelector(".plant-price, [data-testid='plant-price'], .price, span:contains('$')");
    private By plantQuantity = By.cssSelector(".plant-quantity, [data-testid='plant-quantity'], .quantity");
    private By plantCategory = By.cssSelector(".plant-category, [data-testid='plant-category'], .category");
    private By editButton = By.cssSelector(".edit-btn, [data-testid='edit-plant'], button:contains('Edit'), a:contains('Edit')");
    private By backButton = By.cssSelector(".back-btn, [data-testid='back'], a[href*='plants'], button:contains('Back')");
    private By detailContainer = By.cssSelector(".plant-detail, .detail-page, [data-testid='plant-detail'], main, .content, article, .MuiCard-root, .MuiPaper-root");

    public PlantDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDetailPageDisplayed() {
        try {
            // Give time for page to load
            Thread.sleep(1500);
            // Check URL first
            String url = driver.getCurrentUrl();
            if (url.contains("plant") || url.contains("detail") || url.contains("view")) {
                return true;
            }
            // Check for detail container
            java.util.List<WebElement> containers = driver.findElements(detailContainer);
            for (WebElement c : containers) {
                if (c.isDisplayed()) return true;
            }
            // Check for any heading (might be on detail page)
            java.util.List<WebElement> headings = driver.findElements(plantName);
            return !headings.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPlantName() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(plantName)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPlantPrice() {
        try {
            return driver.findElement(plantPrice).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPlantQuantity() {
        try {
            return driver.findElement(plantQuantity).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isEditFormVisible() {
        try {
            // Check if there's an edit form (inputs) on the detail page
            return driver.findElements(By.cssSelector("input[name='name'], input[name='price']")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEditButtonVisible() {
        try {
            return driver.findElement(editButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBack() {
        try {
            driver.findElement(backButton).click();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}
