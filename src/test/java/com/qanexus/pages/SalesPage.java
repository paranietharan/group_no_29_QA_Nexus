package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SalesPage {

    private final WebDriver driver;

  
    private final By salesMenu = By.linkText("Sales");
    
    private final By noSalesFoundMessage = By.xpath("//*[contains(normalize-space(),'No sales found')]");
    private final By salesTableHeaders = By.cssSelector("table thead tr");
private final By sellPlantButton =
        By.xpath("//button[contains(.,'Sell Plant')] | //a[contains(.,'Sell Plant')]");

// Sales table rows (to ensure sales exist)
private final By salesRows = By.xpath("//table/tbody/tr");

// Delete action/button/link inside Actions column (generic match)
private final By deleteAction = By.xpath(
        "//table/tbody/tr//button[contains(translate(normalize-space(.),'DELETE','delete'),'delete')]" +
        " | //table/tbody/tr//a[contains(translate(normalize-space(.),'DELETE','delete'),'delete')]"
);
// Access denied message or page indicator
private final By accessDeniedMessage =
        By.xpath("//*[contains(normalize-space(),'Access Denied') or contains(normalize-space(),'403')]");

// Pagination container (supports common Bootstrap / UI patterns)
private final By paginationContainer = By.cssSelector("ul.pagination, nav[aria-label*='pagination'], .pagination");

// Next / Previous buttons (generic)
private final By nextButton = By.xpath(
        "//a[normalize-space()='Next' or normalize-space()='›' or contains(.,'Next')]" +
        " | //button[normalize-space()='Next' or normalize-space()='›' or contains(.,'Next')]"
);
private final By prevButton = By.xpath(
        "//a[normalize-space()='Previous' or normalize-space()='‹' or contains(.,'Previous')]" +
        " | //button[normalize-space()='Previous' or normalize-space()='‹' or contains(.,'Previous')]"
);
private final By sellPlantSubmitButton =
        By.xpath("//button[@type='submit' or contains(.,'Submit') or contains(.,'Sell') or contains(.,'Save')]");

// Sales table first row (for verifying page change)
private final By firstRowFirstCell = By.xpath("//table/tbody/tr[1]/td[1]");

// Sell Plant form container
private final By sellPlantForm =
        By.xpath("//form[contains(@action,'sales') or .//label[contains(text(),'Plant')]]");

// Plant selection dropdown
private final By plantDropdown =
        By.xpath("//select[contains(@name,'plant') or contains(@id,'plant')]");

// Quantity input field
private final By quantityInput =
        By.xpath("//input[contains(@name,'quantity') or contains(@id,'quantity')]");


// Generic error near quantity (span/div/small with error class OR containing keywords)
private final By quantityErrorMessage =
        By.xpath(
                "//*[contains(@class,'error') or contains(@class,'invalid') or contains(@class,'text-danger')]" +
                "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'quantity')" +
                " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'stock')" +
                " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'available')" +
                " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'exceed')]" +
                " | //input[contains(@name,'quantity') or contains(@id,'quantity')]/following::*[1]"
        );




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
public boolean isSellPlantButtonVisible() {
    return !driver.findElements(sellPlantButton).isEmpty()
            && driver.findElement(sellPlantButton).isDisplayed();
}
public boolean hasSalesRecords() {
    return driver.findElements(salesRows).size() > 0;
}

public boolean isDeleteActionVisible() {
    return !driver.findElements(deleteAction).isEmpty()
            && driver.findElement(deleteAction).isDisplayed();
}

public void navigateToSellPlantPage(String baseUrl) {
    driver.get(baseUrl + "/ui/sales/new");
}

public boolean isAccessDeniedDisplayed() {
    return !driver.findElements(accessDeniedMessage).isEmpty()
            && driver.findElement(accessDeniedMessage).isDisplayed();
}

public void scrollToBottom() {
    ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("window.scrollTo(0, document.body.scrollHeight);");
}

public boolean isPaginationVisible() {
    return !driver.findElements(paginationContainer).isEmpty()
            && driver.findElement(paginationContainer).isDisplayed();
}

public String getFirstRowFirstCellText() {
    return driver.findElement(firstRowFirstCell).getText().trim();
}

public boolean isNextEnabled() {
    return !driver.findElements(nextButton).isEmpty() && driver.findElement(nextButton).isEnabled();
}

public void clickNext() {
    driver.findElement(nextButton).click();
}
public boolean isSellPlantButtonEnabled() {
    return !driver.findElements(sellPlantButton).isEmpty()
            && driver.findElement(sellPlantButton).isEnabled();
}

public void clickSellPlantButton() {

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions
                    .elementToBeClickable(sellPlantButton));

    driver.findElement(sellPlantButton).click();
}


public boolean isSellPlantFormVisible() {
    return !driver.findElements(sellPlantForm).isEmpty()
            && driver.findElement(sellPlantForm).isDisplayed();
}

public boolean isPlantDropdownVisible() {
    return !driver.findElements(plantDropdown).isEmpty()
            && driver.findElement(plantDropdown).isDisplayed();
}

public boolean isQuantityInputVisible() {
    return !driver.findElements(quantityInput).isEmpty()
            && driver.findElement(quantityInput).isDisplayed();
}
public void selectFirstPlantOption() {
    org.openqa.selenium.WebElement dropdown = driver.findElement(plantDropdown);
    org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);
    // Select first non-empty option
    for (org.openqa.selenium.WebElement opt : select.getOptions()) {
        String val = opt.getAttribute("value");
        String text = opt.getText();
        if ((val != null && !val.trim().isEmpty()) || (text != null && !text.trim().isEmpty())) {
            select.selectByVisibleText(text);
            return;
        }
    }
    // fallback
    select.selectByIndex(1);
}

public int getAvailableStockFromDropdownOption() {
    // Many apps show like "Rose (Stock: 5)" or "Rose - 5"
    org.openqa.selenium.WebElement dropdown = driver.findElement(plantDropdown);
    org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);
    String selectedText = select.getFirstSelectedOption().getText();

    java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(selectedText);
    int lastNumber = -1;
    while (m.find()) lastNumber = Integer.parseInt(m.group(1));

    return lastNumber; // may be -1 if UI doesn't show stock in label
}

public void enterQuantity(String qty) {
    org.openqa.selenium.WebElement qtyInput = driver.findElement(quantityInput);
    qtyInput.clear();
    qtyInput.sendKeys(qty);
}

public String getHtml5QuantityValidationMessage() {
    org.openqa.selenium.WebElement qtyInput = driver.findElement(quantityInput);
    return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("return arguments[0].validationMessage;", qtyInput);
}

public boolean isQuantityErrorVisible() {
    return !driver.findElements(quantityErrorMessage).isEmpty()
            && driver.findElement(quantityErrorMessage).isDisplayed()
            && !driver.findElement(quantityErrorMessage).getText().trim().isEmpty();
}

public boolean isSubmitDisabledOrNotClickable() {
    if (driver.findElements(sellPlantSubmitButton).isEmpty()) return false;
    org.openqa.selenium.WebElement btn = driver.findElement(sellPlantSubmitButton);
    String disabledAttr = btn.getAttribute("disabled");
    return (disabledAttr != null) || !btn.isEnabled();
}





}
