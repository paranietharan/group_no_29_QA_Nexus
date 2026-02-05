package com.qanexus.pages;

import java.util.List;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SalesPage {

    private final WebDriver driver;
private String safeLower(String v) {
    return (v == null) ? "" : v.toLowerCase();
}
  
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

// Sell Plant form container (UNIQUE: must contain plant dropdown AND quantity input)
private final By sellPlantForm =
        By.xpath("//form[.//select[contains(@name,'plant') or contains(@id,'plant')] " +
                "and .//input[contains(@name,'quantity') or contains(@id,'quantity')]]");


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

// ===== ADMIN TC05: Delete sale with confirmation =====


private final By firstRowDeleteButton = By.xpath(
        // any button/a inside actions column (last td) OR anything with aria-label/title including delete/trash
        "//table/tbody/tr[1]/td[last()]//*[self::button or self::a]" +
        " | //table/tbody/tr[1]//*[self::button or self::a]" +
        "[contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'delete')" +
        " or contains(translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'delete')" +
        " or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'trash')" +
        " or contains(translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'trash')]"
);




// Confirmation modal/dialog (covers common UI libraries)
private final By confirmDialog = By.xpath(
        "//*[contains(@role,'dialog') or contains(@class,'modal') or contains(@class,'dialog')]" +
        "[.//button or .//a]"
);

// Confirm/Yes/Delete button inside modal
private final By confirmDeleteButton = By.xpath(
        // in any dialog/modal, look for confirm/yes/delete/ok button
        "//*[contains(@role,'dialog') or contains(@class,'modal') or contains(@class,'dialog')]" +
        "//*[self::button or self::a]" +
        "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'confirm')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'yes')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'delete')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'ok')]" +
        " | " +
        // OR JS alert handled in code
        "//*[false()]"
);

// ===== TC05 locators =====
private String lastDeletedRowSignature = "";


private final By cancelButton = By.xpath(
        "//button[normalize-space()='Cancel' or contains(.,'Cancel')] | " +
        "//a[normalize-space()='Cancel' or contains(.,'Cancel')]"
);

// --- Sell Plant submit button (already exists in your file) ---
// private final By sellPlantSubmitButton = ...

// Optional: success toast/message (kept generic)
private final By successToastOrMsg = By.xpath(
        "//*[contains(@class,'toast') or contains(@class,'alert') or contains(@class,'message')]" +
        "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sold')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'created')" +
        " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'completed')]"
);


public void clickCancelOnSellPlantForm() {
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(cancelButton));
    driver.findElement(cancelButton).click();
}
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

// ===== ADMIN TC05 METHODS =====

public void clickDeleteOnFirstRow() {

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> d.findElements(By.xpath("//table/tbody/tr")).size() > 0);

    List<org.openqa.selenium.WebElement> actions = driver.findElements(firstRowDeleteButton);

    if (actions.isEmpty()) {
        throw new RuntimeException("Could not find any action button/link in first row (Actions column).");
    }

    // Prefer element that looks like delete by attributes/text; else fallback to the last action button
    org.openqa.selenium.WebElement candidate = null;

    for (org.openqa.selenium.WebElement el : actions) {
        String text = (el.getText() == null) ? "" : el.getText().toLowerCase();
        String aria = (el.getAttribute("aria-label") == null) ? "" : el.getAttribute("aria-label").toLowerCase();
        String title = (el.getAttribute("title") == null) ? "" : el.getAttribute("title").toLowerCase();
        String cls = (el.getAttribute("class") == null) ? "" : el.getAttribute("class").toLowerCase();

        if (text.contains("delete") || aria.contains("delete") || title.contains("delete")
                || text.contains("trash") || aria.contains("trash") || title.contains("trash")
                || cls.contains("delete") || cls.contains("trash")) {
            candidate = el;
            break;
        }
    }

    if (candidate == null) {
        candidate = actions.get(actions.size() - 1); // fallback: last action in last column
    }

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(candidate));

    candidate.click();
}






public void confirmDeletion() {
    // 1) JS alert
    try {
        org.openqa.selenium.Alert alert = driver.switchTo().alert();
        alert.accept();
        return;
    } catch (org.openqa.selenium.NoAlertPresentException ignored) {}

    // 2) Modal confirm button
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(confirmDeleteButton));
    driver.findElement(confirmDeleteButton).click();
}

public int getSalesRowCount() {
    return driver.findElements(salesRows).size();
}

public String getFirstRowSignature() {
    // Use first row text as a signature (stable enough for UI validation)
    if (driver.findElements(By.xpath("//table/tbody/tr[1]")).isEmpty()) return "";
    return driver.findElement(By.xpath("//table/tbody/tr[1]")).getText().trim();
}



// ===== TC05 helper methods =====



public void clickDeleteOnAnyRow() {

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> d.findElements(salesRows).size() > 0);

    List<org.openqa.selenium.WebElement> rows = driver.findElements(salesRows);

    for (org.openqa.selenium.WebElement row : rows) {

        // capture full row text as signature
        String rowSignature = row.getText().trim();

        List<org.openqa.selenium.WebElement> actionEls =
                row.findElements(By.xpath(".//td[last()]//*[self::button or self::a]"));

        if (actionEls.isEmpty()) continue;

        org.openqa.selenium.WebElement deleteCandidate = null;

        for (org.openqa.selenium.WebElement el : actionEls) {
            String text  = safeLower(el.getText());
            String aria  = safeLower(el.getAttribute("aria-label"));
            String title = safeLower(el.getAttribute("title"));
            String cls   = safeLower(el.getAttribute("class"));
            String testid = safeLower(el.getAttribute("data-testid"));

            boolean looksLikeDelete =
                    text.contains("delete") || text.contains("remove") ||
                    aria.contains("delete") || aria.contains("remove") || aria.contains("trash") ||
                    title.contains("delete") || title.contains("remove") || title.contains("trash") ||
                    cls.contains("delete") || cls.contains("remove") || cls.contains("trash") ||
                    testid.contains("delete") || testid.contains("remove") || testid.contains("trash");

            if (looksLikeDelete) {
                deleteCandidate = el;
                break;
            }
        }

        if (deleteCandidate == null) {
            deleteCandidate = actionEls.get(actionEls.size() - 1); // fallback
        }

        // ✅ Save the row we intend to delete
        lastDeletedRowSignature = rowSignature;

        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(deleteCandidate));

        deleteCandidate.click();
        return;
    }

    throw new RuntimeException("Delete action not found/clickable in any row.");
}

public String clickDeleteOnAnyRowAndGetSignature() {

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> d.findElements(salesRows).size() > 0);

    List<org.openqa.selenium.WebElement> rows = driver.findElements(salesRows);

    for (org.openqa.selenium.WebElement row : rows) {

        String signature = row.getText().trim();

        // actions column = last td
        List<org.openqa.selenium.WebElement> actions =
                row.findElements(By.xpath(".//td[last()]//*[self::button or self::a]"));

        if (actions.isEmpty()) continue;

        org.openqa.selenium.WebElement deleteBtn = null;

        for (org.openqa.selenium.WebElement el : actions) {
            String text  = safeLower(el.getText());
            String aria  = safeLower(el.getAttribute("aria-label"));
            String title = safeLower(el.getAttribute("title"));
            String cls   = safeLower(el.getAttribute("class"));
            String tid   = safeLower(el.getAttribute("data-testid"));

            boolean looksDelete =
                    text.contains("delete") || text.contains("remove") ||
                    aria.contains("delete") || aria.contains("remove") || aria.contains("trash") ||
                    title.contains("delete") || title.contains("remove") || title.contains("trash") ||
                    cls.contains("delete") || cls.contains("remove") || cls.contains("trash") ||
                    tid.contains("delete") || tid.contains("remove") || tid.contains("trash");

            if (looksDelete) {
                deleteBtn = el;
                break;
            }
        }

        if (deleteBtn == null) {
            deleteBtn = actions.get(actions.size() - 1); // fallback
        }

        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(deleteBtn));

        deleteBtn.click();
        return signature;  // ✅ return the row we targeted
    }

    throw new RuntimeException("No Delete action found in any row. Check Actions column locator.");
}

public boolean isRowSignaturePresent(String signature) {
    if (signature == null || signature.trim().isEmpty()) return false;

    for (org.openqa.selenium.WebElement row : driver.findElements(salesRows)) {
        if (row.getText().trim().equals(signature.trim())) return true;
    }
    return false;
}


public void waitForConfirmationPrompt() {
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(6))
            .until(d -> isConfirmationDialogVisible());
}

public boolean isConfirmationDialogVisible() {
    // JS alert
    try {
        driver.switchTo().alert();
        return true;
    } catch (org.openqa.selenium.NoAlertPresentException ignored) {}

    // Modal
    return !driver.findElements(confirmDialog).isEmpty()
            && driver.findElement(confirmDialog).isDisplayed();
}

public String getAlertTextIfPresent() {
    try {
        return driver.switchTo().alert().getText();
    } catch (org.openqa.selenium.NoAlertPresentException e) {
        return "";
    }
}

public void cancelDeletionIfPromptPresent() {
    // JS alert cancel
    try {
        driver.switchTo().alert().dismiss();
        return;
    } catch (org.openqa.selenium.NoAlertPresentException ignored) {}

    // Modal cancel (if exists)
    By cancelBtn = By.xpath(
            "//*[contains(@role,'dialog') or contains(@class,'modal') or contains(@class,'dialog')]" +
            "//*[self::button or self::a]" +
            "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cancel')" +
            " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'no')" +
            " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'close')]"
    );

    if (!driver.findElements(cancelBtn).isEmpty()) {
        driver.findElement(cancelBtn).click();
    }
}

public void clickCancelButton() {

    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(cancelButton));

    driver.findElement(cancelButton).click();
}

public boolean isSalesListPageDisplayed(String baseUrl) {
    return driver.getCurrentUrl().contains("/ui/sales");
}

// ===== ADMIN TC09 METHODS: Stock update after sale =====

public void waitForSellPlantForm() {
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(d -> isSellPlantFormVisible() && isPlantDropdownVisible() && isQuantityInputVisible());
}

public String selectFirstPlantAndReturnVisibleText() {
    org.openqa.selenium.WebElement dropdown = driver.findElement(plantDropdown);
    org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);

    // pick first non-empty option (same logic style as your existing method)
    for (org.openqa.selenium.WebElement opt : select.getOptions()) {
        String val = opt.getAttribute("value");
        String txt = opt.getText();
        if ((val != null && !val.trim().isEmpty()) && (txt != null && !txt.trim().isEmpty())) {
            select.selectByVisibleText(txt);
            return txt.trim();
        }
    }
    // fallback
    select.selectByIndex(1);
    return select.getFirstSelectedOption().getText().trim();
}

/**
 * Normalize plant label so we can re-select the same plant later.
 * Example: "Rose (Stock: 12)" -> "Rose"
 * Example: "Rose - 12" -> "Rose"
 */
public String normalizePlantName(String optionText) {
    if (optionText == null) return "";
    String t = optionText.trim();

    // remove (...) part if exists
    t = t.replaceAll("\\(.*?\\)", "").trim();

    // remove trailing " - number" or " : number" patterns
    t = t.replaceAll("[-:]*\\s*\\d+\\s*$", "").trim();

    return t;
}

/**
 * Extract last number from option text as stock.
 * Returns -1 if no number found.
 */
public int extractStockFromOptionText(String optionText) {
    if (optionText == null) return -1;
    java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(optionText);
    int last = -1;
    while (m.find()) last = Integer.parseInt(m.group(1));
    return last;
}

public void selectPlantByPartialName(String plantName) {
    org.openqa.selenium.WebElement dropdown = driver.findElement(plantDropdown);
    org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);

    String target = (plantName == null) ? "" : plantName.trim().toLowerCase();

    for (org.openqa.selenium.WebElement opt : select.getOptions()) {
        String txt = (opt.getText() == null) ? "" : opt.getText().trim();
        if (!txt.isEmpty() && txt.toLowerCase().contains(target)) {
            select.selectByVisibleText(txt);
            return;
        }
    }
    throw new RuntimeException("Could not re-select plant using name contains: " + plantName);
}

public String getSelectedPlantOptionText() {
    org.openqa.selenium.WebElement dropdown = driver.findElement(plantDropdown);
    org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);
    return select.getFirstSelectedOption().getText().trim();
}

public void clickSellPlantSubmit() {
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(sellPlantSubmitButton));
    driver.findElement(sellPlantSubmitButton).click();
}

public void waitForSaleSubmissionToComplete(String baseUrl) {
    // After submit, most apps redirect back to /ui/sales OR show success msg
    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            .until(d -> d.getCurrentUrl().contains("/ui/sales")
                    || !d.findElements(successToastOrMsg).isEmpty());
}


}
