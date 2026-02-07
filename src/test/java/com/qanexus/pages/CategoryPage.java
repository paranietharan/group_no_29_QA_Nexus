package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

    @FindBy(css = ".error-message, .alert-danger, .validation-error, .text-danger, .invalid-feedback")
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
                WebElement option = driver
                        .findElement(By.xpath("//option[contains(normalize-space(.), '" + parentName + "')]"));
                click(option);
            }
        }
    }

    public void submitCategoryForm() {
        click(submitButton);
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        waitForPageLoad();
    }

    public void cancelCategoryForm() {
        click(cancelButton);
    }

    public void search(String searchTerm) {       
        searchBox.clear();
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        type(searchBox, searchTerm);
        wait.until(ExpectedConditions.attributeToBeNotEmpty(searchBox, "value"));
        applyFilter();
        try {
            Thread.sleep(1000);
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
                WebElement option = driver
                        .findElement(By.xpath("//option[contains(normalize-space(.), '" + parentName + "')]"));
                click(option);
            }
        }
    }

    public void applyFilter() {
        click(applyFilterButton);
    }

    public void clickEditCategory(String categoryName) {
        try {            
            search(categoryName);
            waitForPageLoad();

            WebElement categoryRow = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));            
            WebElement editButton = categoryRow.findElement(By.cssSelector("a.btn-outline-primary"));
            click(editButton);
        } catch (NoSuchElementException e) {
            System.err.println("Category '" + categoryName + "' not found in the table.");
            throw e;
        } catch (Exception e) {
            System.err.println("Failed to click edit for category '" + categoryName + "': " + e.getMessage());
            throw e;
        }
    }

    public void sortByColumn(String columnName) {
        WebElement header = driver.findElement(By.xpath("//th//a[contains(normalize-space(.), '" + columnName + "')]"));
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

    public void clickEditButtonForCategory(String categoryName) { 
                                                                  
        search(categoryName);
        waitForPageLoad();
        wait.withTimeout(java.time.Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));
        WebElement row = driver
                .findElement(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]"));
        WebElement editBtn = row.findElement(By.xpath(".//a[contains(@href, '/ui/categories/edit/')]"));
        click(editBtn);
    }

    public void clickDeleteButtonForCategory(String categoryName) { 
                                                                    
        search(categoryName);
        waitForPageLoad();
        wait.withTimeout(java.time.Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));
        WebElement row = driver
                .findElement(By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]"));
        WebElement deleteBtn = row.findElement(By.xpath(".//button[@title='Delete']"));
        click(deleteBtn);
    }

    public void clickDeleteCategory(String categoryName) {
        try {            
            search(categoryName);
            waitForPageLoad();

            WebElement categoryRow = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[.//td[contains(normalize-space(.), '" + categoryName + "')]]")));
            WebElement deleteButton = categoryRow.findElement(By.cssSelector("button.btn-outline-danger"));
            click(deleteButton);

            try {
                Thread.sleep(500); 
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                alert.accept(); 
            } catch (Exception e) {
                }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            waitForPageLoad();
        } catch (Exception e) {
            System.err.println("Failed to delete category '" + categoryName + "': " + e.getMessage());
            throw e;
        }
    }

    public void clickCategoryName(String categoryName) {
        WebElement editBtn = driver.findElement(By.xpath(
                "//tr[td[2][normalize-space()='" + categoryName + "']]//a[contains(@href,'/edit')]"));

        editBtn.click();
    }

    public boolean isModalDisplayed() {
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmDelete() {
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            alert.accept();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            waitForPageLoad();
        } catch (Exception e) {
            System.err.println("No alert to confirm: " + e.getMessage());
        }
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
        try {
            Thread.sleep(1000);

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.table")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[contains(text(), 'Categories')]")),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//td[contains(normalize-space(.),'No category found')]"))));

            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
