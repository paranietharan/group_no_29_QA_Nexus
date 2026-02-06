package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CategoryPage extends BasePage {
    private final String categoryPath = "/ui/categories";

    @FindBy(css = "table.table")
    private WebElement categoryList;

    @FindBy(xpath = "//a[@href='/ui/categories/add' and contains(normalize-space(.),'Add')]")
    private WebElement addCategoryButton;

    @FindBy(css = "input[name='name'][placeholder*='Search']")
    private WebElement searchBox;

    @FindBy(css = "select[name='parentId']")
    private WebElement parentFilterSelect;

    @FindBy(xpath = "//form[@method='get']//button[@type='submit' and normalize-space()='Search']")
    private WebElement applyFilterButton;

    @FindBy(css = "input[name='name']")
    private WebElement categoryNameInput;

    @FindBy(css = "select[name='parentId']")
    private WebElement parentIdSelect;

    @FindBy(xpath = "//form[contains(@action, '/ui/categories')]//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//form[contains(@action, '/ui/categories')]//a[contains(@class,'btn-secondary') or normalize-space()='Cancel']")
    private WebElement cancelButton;

    @FindBy(css = ".modal, .dialog, [role='dialog']")
    private WebElement modal;

    @FindBy(css = ".modal-content, .dialog-content")
    private WebElement modalContent;

    @FindBy(xpath = "//div[@id='deleteModal']//button[@type='submit' and contains(@class,'btn-danger')] | //button[contains(@class,'btn-danger') and normalize-space()='Delete']")
    private WebElement confirmButton;

    @FindBy(xpath = "//td[contains(normalize-space(.),'No category found')]")
    private WebElement noResultMessage;

    @FindBy(css = ".error-message, .alert-danger, .validation-error")
    private WebElement errorMessage;

    @FindBy(xpath = "//a[contains(@href, '/ui/categories/edit/') and contains(@class, 'btn-outline-primary')]")
    private List<WebElement> editButtons;

    @FindBy(xpath = "//button[contains(@class, 'btn-outline-danger') and @title='Delete']")
    private List<WebElement> deleteButtons;

    @FindBy(xpath = "//table//thead//th")
    private List<WebElement> columnHeaders;

    @FindBy(css = ".pagination, .paging, .paginator")
    private WebElement paginationControls;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        String baseUrl = config.getUiBaseUrl();
        driver.get(baseUrl + categoryPath);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.table")));
    }

    public void navigateTo(String path) {
        String baseUrl = config.getUiBaseUrl();
        driver.get(baseUrl + path);
    }

    public boolean isCategoryListDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(categoryList));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddCategoryButtonVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addCategoryButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPaginationVisible() {
        try {
            return paginationControls.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddCategoryButton() {
        click(addCategoryButton);
    }

    public void enterCategoryName(String name) {
        type(categoryNameInput, name);
    }

    public void selectParentCategory(String parentName) {
        try {
            Select select = new Select(parentIdSelect);
            select.selectByVisibleText(parentName);
        } catch (Exception e) {
            List<WebElement> options = parentIdSelect.findElements(By.tagName("option"));
            boolean clicked = false;
            for (WebElement opt : options) {
                String txt = opt.getText();
                if (txt != null && txt.trim().toLowerCase().contains(parentName.trim().toLowerCase())) {
                    click(opt);
                    clicked = true;
                    break;
                }
            }
            if (!clicked) {
                WebElement option = driver.findElement(By.xpath("//option[contains(normalize-space(.), '" + parentName + "')]") );
                click(option);
            }
        }
    }

    public void submitCategoryForm() {
        click(submitButton);
    }

    public void cancelCategoryForm() {
        click(cancelButton);
    }

    public void search(String searchTerm) {
        // Clear search box first, then type and submit
        searchBox.clear();
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        type(searchBox, searchTerm);
        wait.until(ExpectedConditions.attributeToBeNotEmpty(searchBox, "value"));
        applyFilter(); // Use the filter button instead of Enter
        try {
            Thread.sleep(1000); // Wait for results to filter
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void selectParentFilter(String parentName) {
        try {
            Select select = new Select(parentFilterSelect);
            select.selectByVisibleText(parentName);
        } catch (Exception e) {
            List<WebElement> options = parentFilterSelect.findElements(By.tagName("option"));
            boolean clicked = false;
            for (WebElement opt : options) {
                String txt = opt.getText();
                if (txt != null && txt.trim().toLowerCase().contains(parentName.trim().toLowerCase())) {
                    click(opt);
                    clicked = true;
                    break;
                }
            }
            if (!clicked) {
                WebElement option = driver.findElement(By.xpath("//option[contains(normalize-space(.), '" + parentName + "')]") );
                click(option);
            }
        }
    }

    public void applyFilter() {
        click(applyFilterButton);
    }

    public void sortByColumn(String columnName) {
        WebElement header = driver.findElement(By.xpath("//th//a[contains(normalize-space(.), '" + columnName + "')]") );
        click(header);
    }

    public boolean isNoResultMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(noResultMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getNoResultMessage() {
        try {
            waitForVisibility(noResultMessage);
            return noResultMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            waitForVisibility(errorMessage);
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isCategoryInList(String categoryName) {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//td[contains(text(), '" + categoryName + "')]"));
            return rows.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickEditButtonForCategory(String categoryName) {        // Search first to ensure category is visible in paginated table
        search(categoryName);
        waitForPageLoad();        wait.withTimeout(java.time.Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]"));
        WebElement editBtn = row.findElement(By.xpath(".//a[contains(@href, '/ui/categories/edit/')]"));
        click(editBtn);
    }

    public void clickDeleteButtonForCategory(String categoryName) {        // Search first to ensure category is visible in paginated table
        search(categoryName);
        waitForPageLoad();        wait.withTimeout(java.time.Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));
        WebElement row = driver.findElement(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]"));
        WebElement deleteBtn = row.findElement(By.xpath(".//button[@title='Delete']") );
        click(deleteBtn);
    }

    public void clickCategoryName(String categoryName) {
        WebElement categoryLink = driver.findElement(By.xpath("//a[contains(text(), '" + categoryName + "')]"));
        click(categoryLink);
    }

    public boolean isModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(modal));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmDelete() {
        click(confirmButton);
        wait.until(ExpectedConditions.invisibilityOf(modal));
    }

    public boolean isEditDeleteButtonsVisible() {
        try {
            return editButtons.size() > 0 && deleteButtons.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areEditDeleteButtonsHidden() {
        return editButtons.size() == 0 && deleteButtons.size() == 0;
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitForPageLoad() {
        // tolerate different table containers or empty-state cells
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.table")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(normalize-space(.),'No category found')]"))
        ));
    }

    public boolean isCategoryVisible(String categoryName) {
        try {
            WebElement category = driver.findElement(By.xpath("//td[contains(text(), '" + categoryName + "')]"));
            return category.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
