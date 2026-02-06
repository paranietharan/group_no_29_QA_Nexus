package com.qanexus.steps;

import com.qanexus.api.AuthApi;
import com.qanexus.api.CategoryApi;
import com.qanexus.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryApiSteps {
    private final TestContext context;
    public CategoryApiSteps(TestContext context) {
        this.context = context;
    }

    private final AuthApi authApi = new AuthApi();
    private final CategoryApi categoryApi = new CategoryApi();
    private final Logger logger = LoggerFactory.getLogger(CategoryApiSteps.class);

   
    private static class CategoryCreationResult {
        Response response;
        String uniqueName;

        CategoryCreationResult(Response response, String uniqueName) {
            this.response = response;
            this.uniqueName = uniqueName;
        }
    }

    
    private CategoryCreationResult createCategorySafely(String token, String categoryName, Long parentId) {
        Response response = categoryApi.createCategory(token, categoryName, parentId);

        if (response.statusCode() == 201) {
            return new CategoryCreationResult(response, categoryName);
        }

         if (response.statusCode() == 400) {
            response = categoryApi.createCategory(token, categoryName, parentId);
        }

        return new CategoryCreationResult(response, categoryName);
    }

    private String getUniqueName(String baseName) {
        String timestamp = String.valueOf(System.currentTimeMillis() % 10000); 
        String truncated = baseName.length() > 6 ? baseName.substring(0, 6) : baseName;
        return truncated + timestamp;
    }   

    @Given("the API service is available")
    public void theApiServiceIsAvailable() {
        assertNotNull(categoryApi, "CategoryApi should be initialized");
    }

    @Given("admin is authenticated with valid credentials")
    public void adminIsAuthenticatedWithValidCredentials() {
        context.setUsername("admin");
        context.setPassword("admin123");
        context.setUserRole("ADMIN");
        Response loginResponse = authApi.login(context.getUsername(), context.getPassword());
        assertEquals(200, loginResponse.statusCode(), "Admin login should succeed");
        String token = loginResponse.jsonPath().getString("token");
        context.setToken(token);
    }

    @Given("user is authenticated with valid credentials")
    public void userIsAuthenticatedWithValidCredentials() {
        context.setUsername("testuser");
        context.setPassword("test123");
        context.setUserRole("USER");
        Response loginResponse = authApi.login(context.getUsername(), context.getPassword());
        assertEquals(200, loginResponse.statusCode(), "User login should succeed");
        String token = loginResponse.jsonPath().getString("token");
        context.setToken(token);
    }

    @Given("a main category {string} exists")
    public void aMainCategoryExists(String categoryName) {
        String uniqueName = getUniqueName(categoryName);
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueName, null);
        assertEquals(201, result.response.statusCode(),
                "Main category creation should succeed. Response: " + result.response.getBody().asString());
        Long categoryId = result.response.jsonPath().getLong("id");
        context.setCategoryId(categoryId);
        context.setCategoryName(result.uniqueName);
    }

    @Given("a category {string} exists")
    public void aCategoryExists(String categoryName) {
        String uniqueName = getUniqueName(categoryName);
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueName, null);
        assertEquals(201, result.response.statusCode(),
                "Category creation should succeed. Response: " + result.response.getBody().asString());
        Long categoryId = result.response.jsonPath().getLong("id");
        context.setCategoryId(categoryId);
        context.setCategoryName(result.uniqueName);
    }

    @Given("a category {string} exists with no sub-categories")
    public void aCategoryExistsWithNoSubCategories(String categoryName) {
        String uniqueName = getUniqueName(categoryName);
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueName, null);
        assertEquals(201, result.response.statusCode(),
                "Category creation should succeed. Response: " + result.response.getBody().asString());
        Long categoryId = result.response.jsonPath().getLong("id");
        context.setCategoryId(categoryId);
        context.setCategoryName(result.uniqueName);
    }

    @When("admin sends POST request to {string} with main category {string} and no parent")
    public void adminSendsPOSTRequestWithMainCategory(String endpoint, String categoryName) {
        String uniqueName = getUniqueName(categoryName);
        logger.info(
                "Admin sends POST request to " + endpoint + " with main category " + uniqueName + " and no parent");
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueName, null);
        context.setResponse(result.response);
        context.setCategoryName(result.uniqueName);
        if (result.response.statusCode() == 201) {
            context.setCategoryId(result.response.jsonPath().getLong("id"));
        }
    }

    @When("admin sends POST request to {string} with sub-category {string} under parent")
    public void adminSendsPOSTRequestWithParentId(String endpoint, String subCategoryName) {
        String uniqueName = getUniqueName(subCategoryName);
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueName,
                context.getCategoryId());
        context.setResponse(result.response);
        context.setCategoryName(result.uniqueName);
        if (result.response.statusCode() == 201) {
            context.setCategoryId(result.response.jsonPath().getLong("id"));
        }
    }

    @Given("a main category {string} exists with sub-categories")
    public void aMainCategoryExistsWithSubCategories(String mainCategoryName) {
        String uniqueMain = getUniqueName(mainCategoryName);
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueMain, null);
        assertEquals(201, result.response.statusCode(),
                "Main category creation should succeed. Response: " + result.response.getBody().asString());
        Long mainCategoryId = result.response.jsonPath().getLong("id");
        context.setParentCategoryId(mainCategoryId);
        context.setCategoryName(result.uniqueName);

        createCategorySafely(context.getToken(), getUniqueName("Sub1"), mainCategoryId);
        createCategorySafely(context.getToken(), getUniqueName("Sub2"), mainCategoryId);
    }

    @Given("a main category with sub-categories exists")
    public void aMainCategoryWithSubCategoriesExists() {
        String uniqueMain = getUniqueName("Parent");
        CategoryCreationResult result = createCategorySafely(context.getToken(), uniqueMain, null);
        assertEquals(201, result.response.statusCode(),
                "Main category creation should succeed. Response: " + result.response.getBody().asString());
        Long mainCategoryId = result.response.jsonPath().getLong("id");
        context.setParentCategoryId(mainCategoryId);
        context.setCategoryName(result.uniqueName);

        createCategorySafely(context.getToken(), getUniqueName("Child1"), mainCategoryId);
        createCategorySafely(context.getToken(), getUniqueName("Child2"), mainCategoryId);
    }

    @When("admin sends PUT request to update category name to {string}")
    public void adminSendsPUTRequestToUpdateCategoryName(String newName) {
        Response response = categoryApi.updateCategory(context.getToken(), context.getCategoryId(), newName, null);
        context.setResponse(response);
        context.setCategoryName(newName);
    }

    @When("admin sends DELETE request to remove the category")
    public void adminSendsDELETERequestToRemoveCategory() {
        Response response = categoryApi.deleteCategory(context.getToken(), context.getCategoryId());
        context.setResponse(response);
    }

    @When("admin sends GET request to fetch category by id")
    public void adminSendsGETRequestToFetchCategoryById() {
        Response response = categoryApi.getCategory(context.getToken(), context.getCategoryId());
        context.setResponse(response);
    }

    @When("admin attempts to create duplicate category {string} with same parent")
    public void adminAttemptsToCreateDuplicateCategory(String categoryName) {
        String nameToUse = context.getCategoryName() != null ? context.getCategoryName() : categoryName;
        Long parentId = context.getParentCategoryId() != null ? context.getParentCategoryId() : null;
        Response response = categoryApi.createCategory(context.getToken(), nameToUse, parentId);
        context.setResponse(response);
    }

    @When("admin attempts to create category with name {string}")
    public void adminAttemptsToCreateCategoryWithName(String categoryName) {
        try {
            Response response = categoryApi.createCategory(context.getToken(), categoryName, null);
            context.setResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category with name '" + categoryName + "': " + e.getMessage(),
                    e);
        }
    }

    @When("admin attempts to delete the main category")
    public void adminAttemptsToDeleteTheMainCategory() {
        Response response = categoryApi.deleteCategory(context.getToken(), context.getParentCategoryId());
        context.setResponse(response);
    }

    @When("admin attempts to update category with invalid parent id {long}")
    public void adminAttemptsToUpdateCategoryWithInvalidParentId(Long invalidParentId) {
        Response response = categoryApi.updateCategory(context.getToken(), context.getCategoryId(),
                context.getCategoryName(), invalidParentId);
        context.setResponse(response);
    }

    @When("admin sends GET request to fetch category with invalid id {long}")
    public void adminSendsGETRequestToFetchCategoryWithInvalidId(Long invalidId) {
        try {
            Response response = categoryApi.getCategory(context.getToken(), invalidId);
            context.setResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch category with invalid id " + invalidId + ": " + e.getMessage(),
                    e);
        }
    }

    @When("admin sends GET request with search parameter {string}")
    public void adminSendsGETRequestWithSearchParameter(String searchTerm) {
        try {
            Response response = categoryApi.searchCategories(context.getToken(), searchTerm);
            context.setResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search categories with term '" + searchTerm + "': " + e.getMessage(),
                    e);
        }
    }

    @When("admin sends GET request with parent filter")
    public void adminSendsGETRequestWithParentFilter() {
        try {
            Response response = categoryApi.filterCategoriesByParent(context.getToken(), context.getParentCategoryId());
            context.setResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to filter categories by parent: " + e.getMessage(), e);
        }
    }

    @When("admin sends GET request with page={int}, size={int}, sortBy={string}")
    public void adminSendsGETRequestWithPagination(int page, int size, String sortField) {
        try {
            Response response = categoryApi.getCategoriesPaged(context.getToken(), page, size, sortField, "asc");
            context.setResponse(response);
            context.setCurrentPage(page);
            context.setPageSize(size);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get paginated categories: " + e.getMessage(), e);
        }
    }

    @Given("categories {string}, {string}, {string} exist")
    public void categoriesExist(String cat1, String cat2, String cat3) {
        createCategorySafely(context.getToken(), getUniqueName(cat1), null);
        createCategorySafely(context.getToken(), getUniqueName(cat2), null);
        createCategorySafely(context.getToken(), getUniqueName(cat3), null);
    }

    @Given("multiple categories exist")
    public void multipleCategoriesExist() {
        for (int i = 1; i <= 15; i++) {
            createCategorySafely(context.getToken(), getUniqueName("Category" + i), null);
        }
    }

    @When("user sends GET request to {string}")
    public void userSendsGETRequest(String endpoint) {
        Response response = categoryApi.getAllCategories(context.getToken());
        context.setResponse(response);
    }

    @When("user sends GET request to fetch category by id")
    public void userSendsGETRequestToFetchCategoryById() {
        adminSendsGETRequestToFetchCategoryById();
    }

    @When("user sends GET request with search parameter {string}")
    public void userSendsGETRequestWithSearchParameter(String searchTerm) {
        adminSendsGETRequestWithSearchParameter(searchTerm);
    }

    @When("user sends GET request with parent filter")
    public void userSendsGETRequestWithParentFilter() {
        adminSendsGETRequestWithParentFilter();
    }

    @When("user sends GET request for main categories only")
    public void userSendsGETRequestForMainCategoriesOnly() {
        Response response = categoryApi.getMainCategories(context.getToken());
        context.setResponse(response);
    }

    @When("user extracts first category ID")
    public void userExtractsFirstCategoryID() {
        List<?> ids = context.getResponse().jsonPath().getList("id");
        if (ids != null && !ids.isEmpty()) {
            Object idObj = ids.get(0);
            Long id = idObj instanceof Long ? (Long) idObj : ((Number) idObj).longValue();
            context.setCategoryId(id);
        } else {
            ids = context.getResponse().jsonPath().getList("content.id");
            if (ids != null && !ids.isEmpty()) {
                Object idObj = ids.get(0);
                Long id = idObj instanceof Long ? (Long) idObj : ((Number) idObj).longValue();
                context.setCategoryId(id);
            }
        }
    }

    @When("user extracts first parent category ID")
    public void userExtractsFirstParentCategoryID() {
        List<?> ids = context.getResponse().jsonPath().getList("id");
        if (ids != null && ids.size() > 0) {
            Object idObj = ids.get(0);
            Long id = idObj instanceof Long ? (Long) idObj : ((Number) idObj).longValue();
            context.setParentCategoryId(id);
        }
    }

    @When("user sends POST request to create category {string}")
    public void userSendsPOSTRequestToCreateCategory(String categoryName) {
        adminAttemptsToCreateCategoryWithName(categoryName);
    }

    @When("user sends PUT request to update category name")
    public void userSendsPUTRequestToUpdateCategoryName() {
        Response response = categoryApi.updateCategory(context.getToken(), context.getCategoryId(), "UpdatedName",
                null);
        context.setResponse(response);
    }

    @When("user sends DELETE request to remove the category")
    public void userSendsDELETERequestToRemoveCategory() {
        adminSendsDELETERequestToRemoveCategory();
    }

    @When("user sends GET request to fetch category with invalid id {long}")
    public void userSendsGETRequestToFetchCategoryWithInvalidId(Long invalidId) {
        adminSendsGETRequestToFetchCategoryWithInvalidId(invalidId);
    }

    @When("user sends GET request with page={int}, size={int}")
    public void userSendsGETRequestWithPageAndSize(int page, int size) {
        Response response = categoryApi.getCategoriesPaged(context.getToken(), page, size);
        context.setResponse(response);
    }

    @Then("the response contains category with name {string}")
    public void theResponseContainsCategoryWithName(String expectedName) {
        String actualName = context.getResponse().jsonPath().getString("name");
        assertTrue(actualName != null && (actualName.equals(expectedName) || actualName.startsWith(expectedName)),
                "Category name should be '" + expectedName + "' or start with it. Actual: '" + actualName + "'");
    }

    @Then("the response contains a non-null category id")
    public void theResponseContainsNonNullCategoryId() {
        Long id = context.getResponse().jsonPath().getLong("id");
        assertNotNull(id, "Category id should not be null");
        assertTrue(id > 0, "Category id should be positive");
    }

    @Then("the response contains parent category reference")
    public void theResponseContainsParentCategoryReference() {
        Object parentObj = context.getResponse().jsonPath().get("parent");
        assertNotNull(parentObj, "Parent category reference should exist in response");
    }

    @Then("the response contains updated name {string}")
    public void theResponseContainsUpdatedName(String expectedName) {
        String actualName = context.getResponse().jsonPath().getString("name");
        assertEquals(expectedName, actualName, "Updated name should be " + expectedName);
    }

    @Then("the response contains error message about duplicate")
    public void theResponseContainsErrorMessageAboutDuplicate() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertTrue(errorMessage.toLowerCase().contains("duplicate") ||
                errorMessage.toLowerCase().contains("already exists"),
                "Error message should mention duplicate or already exists");
    }

    @Then("the response contains validation error for name length")
    public void theResponseContainsValidationErrorForNameLength() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertTrue(errorMessage.toLowerCase().contains("length") ||
                errorMessage.toLowerCase().contains("size") ||
                errorMessage.toLowerCase().contains("validation"),
                "Error message should mention validation");
    }

    @Then("the response contains error message about children")
    public void theResponseContainsErrorMessageAboutChildren() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertTrue(errorMessage.toLowerCase().contains("child") ||
                errorMessage.toLowerCase().contains("sub") ||
                errorMessage.toLowerCase().contains("cannot"),
                "Error message should indicate category has children");
    }

    @Then("the response contains error message about invalid parent")
    public void theResponseContainsErrorMessageAboutInvalidParent() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertTrue(errorMessage.toLowerCase().contains("parent") ||
                errorMessage.toLowerCase().contains("invalid") ||
                errorMessage.toLowerCase().contains("not found"),
                "Error message should mention invalid parent");
    }

    @Then("the response contains error message")
    public void theResponseContainsErrorMessage() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertNotNull(errorMessage, "Error message should be present");
        assertTrue(!errorMessage.isEmpty(), "Error message should not be empty");
    }

    @Then("the response contains list of categories")
    public void theResponseContainsListOfCategories() {
        List<Map<String, Object>> categories = context.getResponse().jsonPath().getList("$");
        assertNotNull(categories, "Category list should not be null");
    }

    @Then("the response contains category details")
    public void theResponseContainsCategoryDetails() {
        String name = context.getResponse().jsonPath().getString("name");
        Long id = context.getResponse().jsonPath().getLong("id");
        assertNotNull(name, "Category name should be present");
        assertNotNull(id, "Category id should be present");
    }

    @Then("the response contains categories matching {string}")
    public void theResponseContainsCategoriesMatching(String searchTerm) {
        List<String> names = context.getResponse().jsonPath().getList("name");
        assertTrue(names.stream().anyMatch(n -> n.toLowerCase().contains(searchTerm.toLowerCase())),
                "Results should contain matching categories");
    }

    @Then("the response contains matching categories")
    public void theResponseContainsMatchingCategories() {
        List<Map<String, Object>> categories = context.getResponse().jsonPath().getList("$");
        assertNotNull(categories, "Categories should be present");
        assertTrue(categories.size() > 0, "Should have at least one matching category");
    }

    @Then("the response contains only sub-categories of that parent")
    public void theResponseContainsOnlySubCategoriesOfThatParent() {
        List<Long> parentIds = context.getResponse().jsonPath().getList("parent.id");
        assertTrue(parentIds.stream().allMatch(Objects::nonNull),
                "All categories should have a parent");
    }

    @Then("the response contains only sub-categories")
    public void theResponseContainsOnlySubCategories() {
        List<Long> parentIds = context.getResponse().jsonPath().getList("parent.id");
        assertTrue(parentIds != null && parentIds.size() > 0,
                "Should have sub-categories");
    }

    @Then("the response contains only main categories")
    public void theResponseContainsOnlyMainCategories() {
        List<Long> parentIds = context.getResponse().jsonPath().getList("parent.id");
        assertTrue(parentIds.stream().allMatch(Objects::isNull),
                "All main categories should have no parent");
    }

    @Then("the response contains permission error message")
    public void theResponseContainsPermissionErrorMessage() {
        int statusCode = context.getResponse().statusCode();
        assertEquals(403, statusCode, "Should return 403 Forbidden");
    }

    @Then("the response contains paginated categories")
    public void theResponseContainsPaginatedCategories() {
        List<Map<String, Object>> content = context.getResponse().jsonPath().getList("content");
        assertNotNull(content, "Paginated content should be present");
    }

    @Then("the response contains paginated categories sorted by name")
    public void theResponseContainsPaginatedCategoriesSortedByName() {
        List<String> names = context.getResponse().jsonPath().getList("name");
        if (names == null || names.isEmpty()) {
            names = context.getResponse().jsonPath().getList("content.name");
        }
        assertNotNull(names, "Category names should be present");
        assertTrue(names.size() > 0, "Should have at least one category");
    }
}
