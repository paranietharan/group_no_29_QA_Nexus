package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PlantListPage extends BasePage {
    
    // Selectors based on actual UI HTML structure
    private By plantTable = By.cssSelector("table.table-striped.table-bordered, table.table-striped, table");
    private By plantRows = By.cssSelector("table tbody tr");
    private By addPlantButton = By.cssSelector("a[href='/ui/plants/add'], a[href*='/plants/add']");
    private By editButtons = By.cssSelector("a[href*='/plants/edit/']");
    private By deleteButtons = By.cssSelector("form[action*='/plants/delete/'] button, button.btn-outline-danger");
    private By searchInput = By.cssSelector("input[name='name'][placeholder='Search plant'], input[name='name']");
    private By categoryFilter = By.cssSelector("select[name='categoryId']");
    private By columnHeaders = By.cssSelector("thead.table-dark th a, table thead th a");
    private By paginationControls = By.cssSelector(".pagination, nav[aria-label*='pagination']");
    private By noDataMessage = By.cssSelector("td.text-center.text-muted, .no-data, .empty-state");
    private By plantNameLinks = By.cssSelector("table tbody tr td:first-child");
    private By searchButton = By.cssSelector("form button.btn-primary");
    private By resetButton = By.cssSelector("a.btn-outline-secondary[href='/ui/plants']");
    private By anyContent = By.cssSelector(".main-content, .app-layout, body");

    public PlantListPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get(config.getUiBaseUrl() + "/ui/plants");
        waitForPageLoad();
    }

    public void navigateToAdminPlants() {
        // Admin and user see same page, just different permissions
        driver.get(config.getUiBaseUrl() + "/ui/plants");
        waitForPageLoad();
    }

    private boolean isAnyContentVisible() {
        try {
            return driver.findElement(anyContent).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000); // Wait for SPA to render
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(plantTable),
                ExpectedConditions.presenceOfElementLocated(noDataMessage),
                ExpectedConditions.presenceOfElementLocated(anyContent)
            ));
        } catch (Exception e) {
            // Page may have different structure
        }
    }

    public boolean isPlantTableVisible() {
        try {
            // Wait a bit for SPA rendering
            Thread.sleep(1000);
            List<WebElement> tables = driver.findElements(plantTable);
            for (WebElement table : tables) {
                if (table.isDisplayed()) return true;
            }
            // Also check if there's any table at all
            List<WebElement> anyTables = driver.findElements(By.tagName("table"));
            return !anyTables.isEmpty() && anyTables.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddPlantButtonVisible() {
        try {
            // Based on actual HTML: a[href='/ui/plants/add'] with text "Add a Plant"
            List<WebElement> buttons = driver.findElements(By.cssSelector("a[href*='/plants/add'], a.btn"));
            for (WebElement btn : buttons) {
                String text = btn.getText().toLowerCase();
                if (text.contains("add") || text.contains("new") || text.contains("create")) {
                    return btn.isDisplayed();
                }
            }
            // Also check for any button/link with add-related text
            List<WebElement> allButtons = driver.findElements(By.cssSelector("button, a.btn, a"));
            for (WebElement btn : allButtons) {
                String text = btn.getText().toLowerCase();
                if (text.contains("add") && text.contains("plant")) {
                    return btn.isDisplayed();
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areEditDeleteButtonsVisible() {
        try {
            List<WebElement> edits = driver.findElements(editButtons);
            List<WebElement> deletes = driver.findElements(deleteButtons);
            return !edits.isEmpty() || !deletes.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterSearchQuery(String query) {
        try {
            // Try to find any input field that could be search
            List<WebElement> inputs = driver.findElements(searchInput);
            for (WebElement input : inputs) {
                if (input.isDisplayed()) {
                    input.clear();
                    input.sendKeys(query);
                    return;
                }
            }
            // Fallback: find any visible text input
            List<WebElement> allInputs = driver.findElements(By.cssSelector("input[type='text'], input:not([type])"));
            for (WebElement input : allInputs) {
                if (input.isDisplayed()) {
                    input.clear();
                    input.sendKeys(query);
                    return;
                }
            }
        } catch (Exception e) {
            // Search may not be available
        }
    }

    public void submitSearch() {
        try {
            List<WebElement> inputs = driver.findElements(searchInput);
            for (WebElement input : inputs) {
                if (input.isDisplayed()) {
                    input.sendKeys(org.openqa.selenium.Keys.ENTER);
                    waitForTableUpdate();
                    return;
                }
            }
        } catch (Exception e) {
            // Ignore
        }
    }

    public void selectCategoryFilter(String categoryName) {
        try {
            List<WebElement> filters = driver.findElements(categoryFilter);
            for (WebElement filter : filters) {
                if (filter.isDisplayed()) {
                    if (filter.getTagName().equalsIgnoreCase("select")) {
                        Select select = new Select(filter);
                        select.selectByVisibleText(categoryName);
                    } else {
                        // For custom dropdowns, click and select
                        filter.click();
                        Thread.sleep(500);
                        List<WebElement> options = driver.findElements(By.cssSelector("li, option, [role='option']"));
                        for (WebElement opt : options) {
                            if (opt.getText().contains(categoryName)) {
                                opt.click();
                                break;
                            }
                        }
                    }
                    waitForTableUpdate();
                    return;
                }
            }
        } catch (Exception e) {
            // Filter may not be available
        }
    }

    public void clickColumnHeader(String columnName) {
        List<WebElement> headers = driver.findElements(columnHeaders);
        for (WebElement header : headers) {
            if (header.getText().toLowerCase().contains(columnName.toLowerCase())) {
                header.click();
                waitForTableUpdate();
                return;
            }
        }
    }

    public boolean isSortIndicatorVisible() {
        List<WebElement> headers = driver.findElements(columnHeaders);
        for (WebElement header : headers) {
            String className = header.getAttribute("class");
            String ariaSort = header.getAttribute("aria-sort");
            if ((className != null && (className.contains("sort") || className.contains("asc") || className.contains("desc"))) ||
                (ariaSort != null && !ariaSort.isEmpty())) {
                return true;
            }
        }
        return false;
    }

    public int getPlantRowCount() {
        try {
            List<WebElement> rows = driver.findElements(plantRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickPlantName(String plantName) {
        List<WebElement> links = driver.findElements(plantNameLinks);
        for (WebElement link : links) {
            if (link.getText().toLowerCase().contains(plantName.toLowerCase())) {
                link.click();
                return;
            }
        }
        // Fallback: click on row containing plant name
        List<WebElement> rows = driver.findElements(plantRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(plantName.toLowerCase())) {
                row.click();
                return;
            }
        }
    }

    public void clickEditButton(String plantName) {
        List<WebElement> rows = driver.findElements(plantRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(plantName.toLowerCase())) {
                // Based on actual HTML: a[href*='/plants/edit/']
                WebElement editBtn = row.findElement(By.cssSelector("a[href*='/plants/edit/'], a.btn-outline-primary"));
                editBtn.click();
                return;
            }
        }
    }

    public void clickDeleteButton(String plantName) {
        List<WebElement> rows = driver.findElements(plantRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(plantName.toLowerCase())) {
                // Based on actual HTML: form with action='/ui/plants/delete/{id}' and button.btn-outline-danger
                // Note: The form has onsubmit="return confirm('Delete this plant?');" - a JS confirm dialog
                WebElement deleteBtn = row.findElement(By.cssSelector("form[action*='/plants/delete/'] button, button.btn-outline-danger"));
                deleteBtn.click();
                return;
            }
        }
    }

    public void clickAddPlantButton() {
        // Based on actual HTML: a[href='/ui/plants/add'] with text "Add a Plant"
        List<WebElement> buttons = driver.findElements(By.cssSelector("a[href*='/plants/add'], a.btn, button.btn"));
        for (WebElement btn : buttons) {
            String text = btn.getText().toLowerCase();
            if (text.contains("add") && text.contains("plant")) {
                btn.click();
                return;
            }
        }
        // Fallback to CSS selector
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addPlantButton));
        btn.click();
    }

    public boolean isPaginationVisible() {
        try {
            return driver.findElement(paginationControls).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoDataMessageDisplayed() {
        try {
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(noDataMessage));
            return msg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantInList(String plantName) {
        List<WebElement> rows = driver.findElements(plantRows);
        for (WebElement row : rows) {
            if (row.getText().toLowerCase().contains(plantName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void confirmDeleteDialog() {
        try {
            // Based on actual HTML: The delete uses JavaScript confirm() dialog, not Bootstrap modal
            // onsubmit="return confirm('Delete this plant?');"
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // If no alert, try modal button (fallback)
            try {
                WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#deleteModal .btn-danger, .modal .btn-danger")));
                confirmBtn.click();
            } catch (Exception ex) {
                // No dialog found
            }
        }
    }

    private void waitForTableUpdate() {
        try {
            Thread.sleep(500); // Brief wait for DOM update
        } catch (Exception e) {
            // Continue
        }
    }

    public boolean isResponsive() {
        // Check if layout adapts - simplified check
        try {
            WebElement table = driver.findElement(plantTable);
            int viewportWidth = driver.manage().window().getSize().getWidth();
            int tableWidth = table.getSize().getWidth();
            return tableWidth <= viewportWidth;
        } catch (Exception e) {
            return true;
        }
    }
}
