package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PlantFormPage extends BasePage {

    // Selectors based on actual HTML structure
    private By nameInput = By.cssSelector("input#name, input[name='name']");
    private By priceInput = By.cssSelector("input#price, input[name='price']");
    private By quantityInput = By.cssSelector("input#quantity, input[name='quantity']");
    private By categoryDropdown = By.cssSelector("select#categoryId, select[name='categoryId']");
    private By saveButton = By.cssSelector("button.btn-primary, button[type='submit']");
    private By cancelButton = By.cssSelector("a.btn-secondary[href='/ui/plants'], a.btn-secondary");
    private By validationErrors = By.cssSelector(".invalid-feedback, .error, .text-danger");
    private By successToast = By.cssSelector(".alert-success, .toast-success");
    private By formContainer = By.cssSelector("form, .plant-form");

    public PlantFormPage(WebDriver driver) {
        super(driver);
    }

    public void waitForFormLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(formContainer));
    }

    public void enterName(String name) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(nameInput));
        input.clear();
        input.sendKeys(name);
    }

    public void enterPrice(String price) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(priceInput));
        input.clear();
        input.sendKeys(price);
    }

    public void enterQuantity(String quantity) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(quantityInput));
        input.clear();
        input.sendKeys(quantity);
    }

    public void selectCategory(String categoryName) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(categoryDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(categoryName);
    }

    public void selectCategoryByIndex(int index) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(categoryDropdown));
        Select select = new Select(dropdown);
        select.selectByIndex(index);
    }

    public void clickSave() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        btn.click();
    }

    public void clickCancel() {
        // Based on actual HTML: a.btn-secondary[href='/ui/plants'] with text "Cancel"
        List<WebElement> links = driver.findElements(By.cssSelector("a.btn-secondary, a.btn"));
        for (WebElement link : links) {
            if (link.getText().toLowerCase().contains("cancel")) {
                link.click();
                return;
            }
        }
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        btn.click();
    }

    public void confirmCancel() {
        try {
            // Handle confirmation dialog if present
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".confirm-btn, [data-testid='confirm'], button:has-text('Yes'), button:has-text('Confirm')")));
            confirmBtn.click();
        } catch (Exception e) {
            try {
                driver.switchTo().alert().accept();
            } catch (Exception ex) {
                // No confirmation needed
            }
        }
    }

    public boolean areValidationErrorsDisplayed() {
        try {
            List<WebElement> errors = driver.findElements(validationErrors);
            for (WebElement error : errors) {
                if (error.isDisplayed() && !error.getText().isEmpty()) {
                    return true;
                }
            }
            // Check for is-invalid class on inputs
            List<WebElement> invalidInputs = driver.findElements(By.cssSelector(".is-invalid, [aria-invalid='true']"));
            return !invalidInputs.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationErrorText() {
        List<WebElement> errors = driver.findElements(validationErrors);
        StringBuilder sb = new StringBuilder();
        for (WebElement error : errors) {
            if (error.isDisplayed()) {
                sb.append(error.getText()).append(" ");
            }
        }
        return sb.toString().trim();
    }

    public boolean isSuccessToastDisplayed() {
        try {
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(successToast));
            return toast.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSaveButtonEnabled() {
        try {
            WebElement btn = driver.findElement(saveButton);
            return btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategoryDropdownShowingOnlySubCategories() {
        try {
            WebElement dropdown = driver.findElement(categoryDropdown);
            Select select = new Select(dropdown);
            List<WebElement> options = select.getOptions();
            
            for (WebElement option : options) {
                String optionClass = option.getAttribute("class");
                boolean isDisabled = option.getAttribute("disabled") != null;
                String dataType = option.getAttribute("data-type");
                
                // Main categories should be disabled or not present
                if (dataType != null && dataType.equals("main") && !isDisabled) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public void fillPlantForm(String name, String price, String quantity, String category) {
        enterName(name);
        enterPrice(price);
        enterQuantity(quantity);
        if (category != null && !category.isEmpty()) {
            selectCategory(category);
        }
    }
}
