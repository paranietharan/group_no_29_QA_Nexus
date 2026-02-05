package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ModulePage extends BasePage {

    // Page content locators
    private By pageContentLocator = By.cssSelector(".page-content, .main-content, .module-content, .content-area");
    private By pageHeaderLocator = By.cssSelector("h1, .page-header, .page-title");
    
    // Button locators
    private By addButtonLocator = By.cssSelector(".add-btn, .btn-add, button[aria-label='add'], .create-btn");
    private By editButtonsLocator = By.cssSelector(".edit-btn, .btn-edit, button[aria-label='edit'], [data-action='edit']");
    private By deleteButtonsLocator = By.cssSelector(".delete-btn, .btn-delete, button[aria-label='delete'], [data-action='delete']");
    
    // Pagination locators
    private By paginationLocator = By.cssSelector(".pagination, .pagination-controls, nav[aria-label='pagination']");
    private By nextPageLocator = By.cssSelector(".page-link[aria-label='Next'], .next-page, .pagination-next");
    private By prevPageLocator = By.cssSelector(".page-link[aria-label='Previous'], .prev-page, .pagination-prev");
    
    // Loading and data locators
    private By loadingLocator = By.cssSelector(".loading, .spinner, .loader, .progress-bar");
    private By dataRowsLocator = By.cssSelector(".data-row, .table-row, tbody tr, .list-item, [role='row']");
    private By noDataLocator = By.cssSelector(".no-data, .empty-state, .no-results, .zero-state");

    // PageFactory elements as backup
    @FindBy(css = ".page-content, .main-content")
    private WebElement pageContent;
    
    @FindBy(css = ".pagination")
    private WebElement paginationContainer;

    public ModulePage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageDisplayed() {
        try {
            // Check by URL pattern
            String currentUrl = getCurrentUrl();
            boolean isModuleUrl = currentUrl.contains("categories") ||
                                 currentUrl.contains("plants") ||
                                 currentUrl.contains("sales") ||
                                 currentUrl.contains("list") ||
                                 currentUrl.contains("table");
            
            // Check by page content
            boolean hasContent = driver.findElements(pageContentLocator).stream()
                .anyMatch(WebElement::isDisplayed);
            
            // Check by page header
            boolean hasHeader = driver.findElements(pageHeaderLocator).stream()
                .anyMatch(WebElement::isDisplayed);
            
            return isModuleUrl || hasContent || hasHeader;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAddButton() {
        return !driver.findElements(addButtonLocator).isEmpty();
    }

    public boolean hasEditButtons() {
        return !driver.findElements(editButtonsLocator).isEmpty();
    }

    public boolean hasDeleteButtons() {
        return !driver.findElements(deleteButtonsLocator).isEmpty();
    }

    public void clickAddButton() {
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));
        click(addBtn);
    }

    public boolean isAddButtonClickable() {
        try {
            WebElement addBtn = driver.findElement(addButtonLocator);
            return addBtn.isEnabled() && addBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPaginationDisplayed() {
        try {
            WebElement pagination = driver.findElement(paginationLocator);
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickNextPage() {
        if (isPaginationDisplayed()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextPageLocator));
            click(nextBtn);
        }
    }

    public void clickPreviousPage() {
        if (isPaginationDisplayed()) {
            WebElement prevBtn = wait.until(ExpectedConditions.elementToBeClickable(prevPageLocator));
            click(prevBtn);
        }
    }

    public boolean isLoadingIndicatorVisible() {
        try {
            WebElement loading = driver.findElement(loadingLocator);
            return loading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForLoadingToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingLocator));
        } catch (Exception e) {
            // Loading indicator not found or already disappeared
        }
    }

    public int getDataRowCount() {
        try {
            List<WebElement> rows = driver.findElements(dataRowsLocator);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasData() {
        return getDataRowCount() > 0;
    }

    public boolean isNoDataMessageDisplayed() {
        try {
            WebElement noData = driver.findElement(noDataLocator);
            return noData.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageHeader() {
        try {
            WebElement header = driver.findElement(pageHeaderLocator);
            return getText(header);
        } catch (Exception e) {
            return "";
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(pageContentLocator));
    }

    public boolean isDataTableDisplayed() {
        By tableLocator = By.cssSelector("table, .data-table, .table, [role='table']");
        try {
            WebElement table = driver.findElement(tableLocator);
            return table.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // New methods for feature file support
    
    public boolean hasLoadingIndicator() {
        return isLoadingIndicatorVisible();
    }

    public boolean hasEditOptions() {
        return hasEditButtons() || hasDeleteButtons();
    }

}