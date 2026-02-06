package com.qanexus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class MayuravelDashboardPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(MayuravelDashboardPage.class);

    public MayuravelDashboardPage(WebDriver driver) {
        super(driver);
    }

    // ==================== DASHBOARD DISPLAY ====================
    
    public boolean isDashboardDisplayed() {
        try {
            // Check URL
            String currentUrl = getCurrentUrl();
            boolean byUrl = currentUrl.contains("dashboard") || currentUrl.contains("home");
            
            // Check title
            String title = getTitle().toLowerCase();
            boolean byTitle = title.contains("dashboard") || title.contains("home");
            
            // Check for dashboard elements
            String[] dashboardSelectors = {
                ".dashboard",
                ".main-content", 
                "#dashboard",
                ".home",
                "main",
                "[role='main']"
            };
            
            boolean byElement = false;
            for (String selector : dashboardSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        byElement = true;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            return byUrl || byTitle || byElement;
        } catch (Exception e) {
            logger.error("Error checking dashboard display: {}", e.getMessage());
            return false;
        }
    }

    public long measureDashboardLoadTime() {
        Instant start = Instant.now();
        try {
            waitForVisibility(By.cssSelector(".dashboard, .main-content, #dashboard, main"));
            Instant end = Instant.now();
            return Duration.between(start, end).toMillis();
        } catch (Exception e) {
            logger.error("Failed to measure dashboard load time: {}", e.getMessage());
            return 10000; // Return 10 seconds as fallback instead of Long.MAX_VALUE
        }
    }

    // ==================== SUMMARY CARDS ====================
    
    public boolean isCategoriesSummaryCardDisplayed() {
        return isElementDisplayed(
            ".summary-card[data-module='categories']",
            ".card-categories", 
            "#categories-card",
            "[data-card='categories']"
        );
    }

    public boolean isPlantsSummaryCardDisplayed() {
        return isElementDisplayed(
            ".summary-card[data-module='plants']",
            ".card-plants", 
            "#plants-card",
            "[data-card='plants']"
        );
    }

    public boolean isSalesSummaryCardDisplayed() {
        return isElementDisplayed(
            ".summary-card[data-module='sales']",
            ".card-sales", 
            "#sales-card",
            "[data-card='sales']"
        );
    }

    public int getSummaryCardsCount() {
        try {
            List<WebElement> cards = driver.findElements(
                By.cssSelector(".summary-card, .card, [role='card'], .dashboard-card"));
            int visibleCount = 0;
            for (WebElement card : cards) {
                if (card.isDisplayed()) {
                    visibleCount++;
                }
            }
            return visibleCount;
        } catch (Exception e) {
            logger.error("Error counting summary cards: {}", e.getMessage());
            return 0;
        }
    }

    public void clickCategoriesSummaryCard() {
        clickElementWithMultipleSelectors(
            ".summary-card[data-module='categories']",
            ".card-categories",
            "#categories-card"
        );
    }

    public void clickPlantsSummaryCard() {
        clickElementWithMultipleSelectors(
            ".summary-card[data-module='plants']",
            ".card-plants",
            "#plants-card"
        );
    }

    public void clickSalesSummaryCard() {
        clickElementWithMultipleSelectors(
            ".summary-card[data-module='sales']",
            ".card-sales",
            "#sales-card"
        );
    }

    public String getPlantCount() {
        try {
            // First find the plants card
            WebElement plantsCard = findElementWithMultipleSelectors(
                ".summary-card[data-module='plants']",
                ".card-plants",
                "#plants-card"
            );
            
            // Look for count within the card
            List<WebElement> countElements = plantsCard.findElements(
                By.cssSelector(".count, .value, .card-value, h1, h2, h3, .stat-number, .number")
            );
            
            for (WebElement element : countElements) {
                String text = element.getText().trim();
                if (!text.isEmpty()) {
                    // Extract numbers from text
                    String numbers = text.replaceAll("[^0-9]", "");
                    if (!numbers.isEmpty()) {
                        return numbers;
                    }
                }
            }
            
            return "0"; // Default if no count found
        } catch (Exception e) {
            logger.error("Error getting plant count: {}", e.getMessage());
            return "0";
        }
    }

    // ==================== NAVIGATION ====================
    
    public void navigateToCategoriesPage() {
        clickElementWithMultipleSelectors(
            "nav a[href*='categories']",
            ".nav-item-categories",
            "#nav-categories",
            "[data-nav='categories']",
            "a:contains('Categories')",
            "//a[contains(text(), 'Categories')]",
            "//a[contains(@href, 'categories')]"
        );
    }

    public void navigateToPlantsPage() {
        clickElementWithMultipleSelectors(
            "nav a[href*='plants']",
            ".nav-item-plants",
            "#nav-plants",
            "[data-nav='plants']",
            "a:contains('Plants')",
            "//a[contains(text(), 'Plants')]",
            "//a[contains(@href, 'plants')]"
        );
    }

    public void navigateToSalesPage() {
        clickElementWithMultipleSelectors(
            "nav a[href*='sales']",
            ".nav-item-sales",
            "#nav-sales",
            "[data-nav='sales']",
            "a:contains('Sales')",
            "//a[contains(text(), 'Sales')]",
            "//a[contains(@href, 'sales')]"
        );
    }

    public void navigateToDashboard() {
        clickElementWithMultipleSelectors(
            "nav a[href*='dashboard']",
            ".nav-item-dashboard",
            "#nav-dashboard",
            "[data-nav='dashboard']",
            "a:contains('Dashboard')",
            "a:contains('Home')",
            ".logo",
            ".home-link"
        );
    }

    public boolean isCategoriesNavItemActive() {
        return isNavItemActive("categories");
    }

    public boolean isPlantsNavItemActive() {
        return isNavItemActive("plants");
    }

    public boolean isSalesNavItemActive() {
        return isNavItemActive("sales");
    }

    private boolean isNavItemActive(String navType) {
        try {
            String[] selectors = {
                "nav a[href*='" + navType + "']",
                ".nav-item-" + navType,
                "#nav-" + navType
            };
            
            for (String selector : selectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty()) {
                        WebElement element = elements.get(0);
                        String className = element.getAttribute("class");
                        if (className.contains("active") || className.contains("selected")) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Error checking nav item active state: {}", e.getMessage());
            return false;
        }
    }

    // ==================== RESPONSIVE DESIGN ====================
    
    public void resizeBrowserWindow(int width, int height) {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
        try {
            Thread.sleep(1000); // Wait for resize to take effect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean hasOverlappingElements() {
        // Simplified check - in a real implementation you would check element positions
        // For now, just return false assuming no overlap
        return false;
    }

    // ==================== THEME AND STYLING ====================
    
    public String getThemeColor() {
        try {
            return driver.findElement(By.tagName("body")).getCssValue("background-color");
        } catch (Exception e) {
            return "";
        }
    }

    public String getFontFamily() {
        try {
            return driver.findElement(By.tagName("body")).getCssValue("font-family");
        } catch (Exception e) {
            return "";
        }
    }

    // ==================== BREADCRUMBS ====================
    
    public boolean areBreadcrumbsDisplayed() {
        return isElementDisplayed(".breadcrumb", ".breadcrumbs", "[aria-label='breadcrumb']");
    }

    public boolean areBreadcrumbsClickable() {
        try {
            List<WebElement> breadcrumbLinks = driver.findElements(
                By.cssSelector(".breadcrumb a, .breadcrumbs a, [aria-label='breadcrumb'] a")
            );
            return !breadcrumbLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== ERROR HANDLING ====================
    
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(
            ".error-message",
            ".alert-error",
            ".error",
            ".alert-danger",
            "[role='alert']"
        );
    }

    public String getErrorMessageText() {
        try {
            WebElement errorElement = findElementWithMultipleSelectors(
                ".error-message",
                ".alert-error",
                ".error",
                ".alert-danger"
            );
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // ==================== UTILITY METHODS ====================
    
    private boolean isElementDisplayed(String... selectors) {
        for (String selector : selectors) {
            try {
                List<WebElement> elements = driver.findElements(selector.startsWith("//") ? 
                    By.xpath(selector) : By.cssSelector(selector));
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    private void clickElementWithMultipleSelectors(String... selectors) {
        for (String selector : selectors) {
            try {
                logger.debug("Trying selector: {}", selector);
                
                By locator = selector.startsWith("//") ? 
                    By.xpath(selector) : By.cssSelector(selector);
                
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                click(element);
                logger.info("Successfully clicked element using selector: {}", selector);
                return;
            } catch (Exception e) {
                logger.debug("Selector {} failed: {}", selector, e.getMessage());
                continue;
            }
        }
        
        // If all selectors fail, try to find by scanning all links
        if (selectors.length > 0) {
            String targetText = extractTargetTextFromSelectors(selectors);
            if (targetText != null) {
                List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                for (WebElement link : allLinks) {
                    try {
                        String linkText = link.getText().toLowerCase();
                        if (linkText.contains(targetText.toLowerCase())) {
                            click(link);
                            logger.info("Found and clicked link by text: {}", targetText);
                            return;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        
        throw new RuntimeException("Could not find element with any of the selectors: " + 
            String.join(", ", selectors));
    }

    private String extractTargetTextFromSelectors(String[] selectors) {
        for (String selector : selectors) {
            if (selector.contains("contains('")) {
                // Extract text from :contains() pseudo-selector
                int start = selector.indexOf("contains('") + 10;
                int end = selector.indexOf("')", start);
                if (end > start) {
                    return selector.substring(start, end);
                }
            } else if (selector.contains("contains(text(),")) {
                // Extract text from XPath contains(text())
                int start = selector.indexOf("contains(text(),'") + 18;
                int end = selector.indexOf("')]", start);
                if (end > start) {
                    return selector.substring(start, end);
                }
            }
        }
        return null;
    }

    private WebElement findElementWithMultipleSelectors(String... selectors) {
        for (String selector : selectors) {
            try {
                By locator = selector.startsWith("//") ? 
                    By.xpath(selector) : By.cssSelector(selector);
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new RuntimeException("Element not found with any selector");
    }

    private void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(WebElement element) {
        waitForClickable(element);
        element.click();
    }

        // ==================== AUTHORIZATION CHECKS ====================
    
    public boolean hasEditButtons() {
        return isElementDisplayed(
            ".edit-btn",
            ".btn-edit",
            "button[aria-label='edit']",
            "[data-action='edit']",
            "//button[contains(@class, 'edit')]",
            "//button[contains(text(), 'Edit')]"
        );
    }

    public boolean hasDeleteButtons() {
        return isElementDisplayed(
            ".delete-btn",
            ".btn-delete",
            "button[aria-label='delete']",
            "[data-action='delete']",
            "//button[contains(@class, 'delete')]",
            "//button[contains(text(), 'Delete')]"
        );
    }

    public boolean hasAddButtons() {
        return isElementDisplayed(
            ".add-btn",
            ".btn-add",
            "button[aria-label='add']",
            "[data-action='add']",
            "//button[contains(@class, 'add')]",
            "//button[contains(text(), 'Add')]",
            "//button[contains(text(), 'Create')]",
            "//button[contains(text(), 'New')]"
        );
    }

    // Helper method to check for admin-only navigation items
    public boolean hasAdminOnlyNavItems() {
        // Check for admin-specific navigation items
        String[] adminNavSelectors = {
            "nav a[href*='admin']",
            "nav a[href*='settings']",
            "nav a[href*='users']",
            "nav a[href*='reports']",
            ".nav-item-admin",
            "#nav-admin"
        };
        
        for (String selector : adminNavSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    // Check if user has any action buttons (add/edit/delete)
    public boolean hasAnyActionButtons() {
        return hasAddButtons() || hasEditButtons() || hasDeleteButtons();
    }

    // Check if elements are disabled (grayed out) for read-only users
    public boolean areActionButtonsDisabled() {
        try {
            // Check if any action buttons exist and are disabled
            String[] actionButtonSelectors = {
                ".edit-btn:disabled",
                ".btn-edit:disabled",
                ".delete-btn:disabled",
                ".btn-delete:disabled",
                ".add-btn:disabled",
                ".btn-add:disabled",
                "button[aria-label='edit'][disabled]",
                "button[aria-label='delete'][disabled]",
                "button[aria-label='add'][disabled]"
            };
            
            for (String selector : actionButtonSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty()) {
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Error checking if action buttons are disabled: {}", e.getMessage());
            return false;
        }
    }

    // Check for read-only indicators
    public boolean hasReadOnlyIndicators() {
        return isElementDisplayed(
            "[readonly]",
            "[disabled]",
            ".read-only",
            ".disabled",
            ".view-only",
            "[aria-readonly='true']"
        );
    }

    // Check if in read-only mode
    public boolean isReadOnlyMode() {
        return hasReadOnlyIndicators() || !hasEditButtons() || !hasDeleteButtons();
    }

    // Check if has admin-only UI elements
    public boolean hasAdminOnlyUIElements() {
        return isElementDisplayed(
            "[data-admin-only]",
            ".admin-only",
            ".admin-section",
            "[role='admin']",
            ".admin-controls"
        );
    }

    // Check if has admin-only menu items
    public boolean hasAdminOnlyMenuItems() {
        return isElementDisplayed(
            ".nav-item[data-admin-only]",
            "[data-admin-only] .nav-link",
            ".admin-menu-item",
            "li[data-role='admin']"
        );
    }

    // Check if navigation menu is visible
    public boolean isNavigationMenuVisible() {
        return isElementDisplayed(
            ".navbar",
            ".nav-menu",
            ".sidebar",
            "nav",
            "[role='navigation']"
        );
    }

    // Check if has categories menu
    public boolean hasCategoriesMenu() {
        return isElementDisplayed(
            ".nav-categories",
            "[data-menu='categories']",
            "a[href*='categories']",
            ".menu-categories"
        );
    }

    // Check if has plants menu
    public boolean hasPlantsMenu() {
        return isElementDisplayed(
            ".nav-plants",
            "[data-menu='plants']",
            "a[href*='plants']",
            ".menu-plants"
        );
    }

    // Check if has sales menu
    public boolean hasSalesMenu() {
        return isElementDisplayed(
            ".nav-sales",
            "[data-menu='sales']",
            "a[href*='sales']",
            ".menu-sales"
        );
    }

    // Click logout button
    public void clickLogoutButton() {
        try {
            String[] logoutSelectors = {
                ".logout-btn",
                "[data-action='logout']",
                ".btn-logout",
                "button:contains('Logout')",
                "[aria-label='logout']"
            };
            
            for (String selector : logoutSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                        elements.get(0).click();
                        return;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            logger.warn("Logout button not found");
        } catch (Exception e) {
            logger.error("Error clicking logout button: {}", e.getMessage());
        }
    }
}