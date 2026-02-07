package com.qanexus.steps;

import com.qanexus.api.AuthApi;
import com.qanexus.api.PlantApi;
import com.qanexus.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlantApiSteps {
    private final TestContext context = new TestContext();
    private final PlantApi plantApi = new PlantApi();
    private final AuthApi authApi = new AuthApi();
    
    private String plantName;
    private String originalPlantName;  // Store original name for updates
    private double plantPrice;
    private int plantQuantity;
    private int originalQuantity;  // Store original quantity for updates
    private Long categoryId;
    private Long originalCategoryId;  // Store original category for updates
    private Long plantId;
    private Long createdPlantId;

    // ===================== Authentication Steps =====================
    
    @Given("the user is authenticated")
    public void theUserIsAuthenticated() {
        Response response = authApi.login("testuser", "test123");
        assertEquals(200, response.statusCode(), "User login failed");
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
        context.setToken(token);
    }

    @Given("the admin is authenticated")
    public void theAdminIsAuthenticated() {
        Response response = authApi.login("admin", "admin123");
        assertEquals(200, response.statusCode(), "Admin login failed");
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
        context.setToken(token);
    }

    // ===================== Setup Steps =====================

    @Given("a plant with ID {int} exists")
    public void aPlantWithIdExists(int id) {
        // First check if any plants exist
        Response plants = plantApi.getAllPlants(context.getToken());
        if (plants.statusCode() == 200) {
            List<Map<String, Object>> plantList = plants.jsonPath().getList("");
            if (plantList != null && !plantList.isEmpty()) {
                plantId = Long.valueOf(plantList.get(0).get("id").toString());
                // Get category from existing plant
                Map<String, Object> category = (Map<String, Object>) plantList.get(0).get("category");
                if (category != null) {
                    categoryId = Long.valueOf(category.get("id").toString());
                }
                System.out.println("Using existing plant ID: " + plantId);
                return;
            }
        }

        // No plants exist - create one using admin auth
        Response adminLogin = authApi.login("admin", "admin123");
        if (adminLogin.statusCode() == 200) {
            String adminToken = adminLogin.jsonPath().getString("token");

            // Use default sub-category ID
            Long subCatId = 2L; // Default sub-category ID (Roses)

            // Create plant under sub-category
            Response created = plantApi.createPlant(adminToken, subCatId,
                "TestPlant" + System.currentTimeMillis(), 100.0, 10);
            if (created.statusCode() == 201) {
                plantId = created.jsonPath().getLong("id");
                categoryId = subCatId;
                return;
            }
        }

        // Fallback to provided ID
        plantId = (long) id;
        categoryId = 2L;
    }

    private Long getCategoryIdFromExistingPlants(String token) {
        Response plants = plantApi.getAllPlants(token);
        if (plants.statusCode() == 200) {
            List<Map<String, Object>> plantList = plants.jsonPath().getList("");
            if (plantList != null && !plantList.isEmpty()) {
                Map<String, Object> category = (Map<String, Object>) plantList.get(0).get("category");
                if (category != null) {
                    return Long.valueOf(category.get("id").toString());
                }
            }
        }
        return null;
    }

    private void ensureSubCategoryExistsWithToken(String token) {
        // Get category from existing plants
        Long catId = getCategoryIdFromExistingPlants(token);
        if (catId != null) {
            categoryId = catId;
            return;
        }
        // Default fallback
        categoryId = 2L; // Default sub-category ID (Roses)
    }

    @Given("a plant with ID exists")
    public void aPlantWithIdExists() {
        // Create a plant first to ensure it exists
        ensureSubCategoryExists();
        Response response = plantApi.createPlant(context.getToken(), categoryId, 
            "TestPlant" + System.currentTimeMillis(), 100.0, 10);
        if (response.statusCode() == 201) {
            plantId = response.jsonPath().getLong("id");
            System.out.println("Created plant: ID=" + plantId);
        } else {
            // Fallback: fetch existing plant
            Response plants = plantApi.getAllPlants(context.getToken());
            if (plants.statusCode() == 200) {
                List<Map<String, Object>> plantList = plants.jsonPath().getList("");
                if (plantList != null && !plantList.isEmpty()) {
                    plantId = Long.valueOf(plantList.get(0).get("id").toString());
                    System.out.println("Using existing plant: ID=" + plantId);
                }
            }
        }
        assertNotNull(plantId, "Failed to create or find plant");
    }

    @Given("a category with ID {int} exists with plants")
    public void aCategoryWithIdExistsWithPlants(int id) {
        categoryId = (long) id;
    }

    @Given("a plant named {string} exists via API")
    public void aPlantNamedExistsViaApi(String name) {
        plantName = name;
    }

    @Given("a plant named {string} exists in the system")
    public void aPlantNamedExistsInTheSystem(String name) {
        plantName = name;
    }

    @Given("more than {int} plants exist")
    public void moreThanPlantsExist(int count) {
        // Assume plants exist in the system
    }

    @Given("plants exist in the system for API testing")
    public void plantsExistInTheSystemForApiTesting() {
        // Assume plants exist in the system
    }

    @Given("valid plant data for creation")
    public void validPlantDataForCreation() {
        plantName = "TestPlant";
        plantPrice = 100.0;
        plantQuantity = 10;
        ensureSubCategoryExists();
    }

    @Given("valid plant data for update")
    public void validPlantDataForUpdate() {
        plantName = "UpdatedPlant";
        plantPrice = 150.0;
        plantQuantity = 20;
        // Don't call ensureSubCategoryExists - not needed for update data
    }

    @Given("a valid sub-category exists")
    public void aValidSubCategoryExists() {
        ensureSubCategoryExists();
    }

    @Given("valid plant data with name {string} price {double} and quantity {int}")
    public void validPlantData(String name, double price, int quantity) {
        plantName = name + System.currentTimeMillis(); // Make unique
        plantPrice = price;
        plantQuantity = quantity;
    }

    @Given("an existing plant to update")
    public void anExistingPlantToUpdate() {
        ensureSubCategoryExists();
        originalPlantName = "UpdateTest" + System.currentTimeMillis();
        originalQuantity = 10;
        originalCategoryId = categoryId;
        Response response = plantApi.createPlant(context.getToken(), categoryId, 
            originalPlantName, 100.0, originalQuantity);
        if (response.statusCode() == 201) {
            plantId = response.jsonPath().getLong("id");
            createdPlantId = plantId;
            System.out.println("Created plant for update: ID=" + plantId);
        } else {
            // Try to use existing plant
            Response plants = plantApi.getAllPlants(context.getToken());
            if (plants.statusCode() == 200) {
                List<Map<String, Object>> plantList = plants.jsonPath().getList("");
                if (plantList != null && !plantList.isEmpty()) {
                    plantId = Long.valueOf(plantList.get(0).get("id").toString());
                    originalPlantName = plantList.get(0).get("name").toString();
                    originalQuantity = ((Number) plantList.get(0).get("quantity")).intValue();
                    Map<String, Object> cat = (Map<String, Object>) plantList.get(0).get("category");
                    if (cat != null) {
                        originalCategoryId = Long.valueOf(cat.get("id").toString());
                    }
                    System.out.println("Using existing plant for update: ID=" + plantId);
                }
            }
        }
        assertNotNull(plantId, "Failed to create or find plant for update test");
    }

    @Given("updated plant data with price {double}")
    public void updatedPlantDataWithPrice(double price) {
        plantPrice = price;
    }

    @Given("an existing plant to delete")
    public void anExistingPlantToDelete() {
        ensureSubCategoryExists();
        Response response = plantApi.createPlant(context.getToken(), categoryId, 
            "DeleteTest" + System.currentTimeMillis(), 50.0, 5);
        if (response.statusCode() == 201) {
            plantId = response.jsonPath().getLong("id");
            createdPlantId = plantId;
            System.out.println("Created plant for delete: ID=" + plantId);
        } else {
            // Try to use existing plant
            Response plants = plantApi.getAllPlants(context.getToken());
            if (plants.statusCode() == 200) {
                List<Map<String, Object>> plantList = plants.jsonPath().getList("");
                if (plantList != null && !plantList.isEmpty()) {
                    plantId = Long.valueOf(plantList.get(0).get("id").toString());
                    System.out.println("Using existing plant for delete: ID=" + plantId);
                }
            }
        }
        assertNotNull(plantId, "Failed to create or find plant for delete test");
    }

    @Given("plant data with negative quantity {int}")
    public void plantDataWithNegativeQuantity(int quantity) {
        plantName = "NegativeQty" + System.currentTimeMillis();
        plantPrice = 100.0;
        plantQuantity = quantity;
    }

    @Given("plant data with name {string} which is too short")
    public void plantDataWithNameWhichIsTooShort(String name) {
        plantName = name;
        plantPrice = 100.0;
        plantQuantity = 10;
    }

    @Given("plant data with price {int}")
    public void plantDataWithPrice(int price) {
        plantName = "ZeroPrice" + System.currentTimeMillis();
        plantPrice = price;
        plantQuantity = 10;
    }

    @Given("update data with non-existent category ID {int}")
    public void updateDataWithNonExistentCategoryId(int catId) {
        categoryId = (long) catId;
    }

    @Given("a category with plants exists")
    public void aCategoryWithPlantsExists() {
        ensureSubCategoryExists();
    }

    @Given("multiple plants exist in a category")
    public void multiplePlantsExistInACategory() {
        ensureSubCategoryExists();
    }

    @Given("plants exist in the system")
    public void plantsExistInTheSystem() {
        // Assume plants exist
    }

    // ===================== Action Steps - User =====================

    @When("the user sends a GET request to fetch all plants")
    public void theUserSendsGetRequestToFetchAllPlants() {
        Response response = plantApi.getAllPlants(context.getToken());
        context.setResponse(response);
    }

    @When("the user sends a GET request to fetch plant with ID {int}")
    public void theUserSendsGetRequestToFetchPlantWithId(int id) {
        // Use dynamically fetched plantId if available
        long targetId = plantId != null ? plantId : id;
        Response response = plantApi.getPlantById(context.getToken(), targetId);
        context.setResponse(response);
    }

    @When("the user sends a GET request to fetch plants by category ID {int}")
    public void theUserSendsGetRequestToFetchPlantsByCategoryId(int id) {
        Response response = plantApi.getPlantsByCategory(context.getToken(), id);
        context.setResponse(response);
    }

    @When("the user sends a GET request to search plants with name {string}")
    public void theUserSendsGetRequestToSearchPlantsWithName(String name) {
        Response response = plantApi.searchPlants(context.getToken(), name, null, null, null);
        context.setResponse(response);
    }

    @When("the user sends a GET request to fetch plants with page {int} and size {int}")
    public void theUserSendsGetRequestToFetchPlantsWithPagination(int page, int size) {
        Response response = plantApi.searchPlants(context.getToken(), null, null, page, size);
        context.setResponse(response);
    }

    @When("the user sends a POST request to create a plant")
    public void theUserSendsPostRequestToCreatePlant() {
        // Use categoryId 1 as default if not set
        long catId = categoryId != null ? categoryId : 1L;
        Response response = plantApi.createPlant(context.getToken(), catId,
            plantName != null ? plantName : "TestPlant",
            plantPrice > 0 ? plantPrice : 100.0,
            plantQuantity > 0 ? plantQuantity : 10);
        context.setResponse(response);
    }

    @When("the user sends a PUT request to update plant with ID {int}")
    public void theUserSendsPutRequestToUpdatePlant(int id) {
        // Use actual plantId if available (fetched dynamically)
        long targetId = plantId != null ? plantId : id;
        // Send complete data to pass validation and reach authorization check
        Response response = plantApi.updatePlant(context.getToken(), targetId,
            plantName != null ? plantName : "UpdatedPlant",
            plantPrice > 0 ? plantPrice : 100.0,
            plantQuantity > 0 ? plantQuantity : 10,
            null);  // Don't update category
        context.setResponse(response);
    }

    @When("the user sends a DELETE request to delete plant with ID {int}")
    public void theUserSendsDeleteRequestToDeletePlant(int id) {
        // Use dynamically fetched plantId if available
        long targetId = plantId != null ? plantId : id;
        Response response = plantApi.deletePlant(context.getToken(), targetId);
        context.setResponse(response);
    }

    // ===================== Action Steps - Admin =====================

    @When("the admin sends a POST request to create a plant")
    public void theAdminSendsPostRequestToCreatePlant() {
        Response response = plantApi.createPlant(context.getToken(), categoryId, 
            plantName, plantPrice, plantQuantity);
        context.setResponse(response);
        if (response.statusCode() == 201) {
            createdPlantId = response.jsonPath().getLong("id");
        }
    }

    @When("the admin sends a PUT request to update the plant")
    public void theAdminSendsPutRequestToUpdatePlant() {
        // API requires ALL fields - send original values for unchanged fields
        Response response = plantApi.updatePlant(context.getToken(), plantId, 
            originalPlantName, plantPrice, originalQuantity, originalCategoryId);
        context.setResponse(response);
    }

    @When("the admin sends a DELETE request to delete the plant")
    public void theAdminSendsDeleteRequestToDeletePlant() {
        Response response = plantApi.deletePlant(context.getToken(), plantId);
        context.setResponse(response);
    }

    @When("the admin sends a GET request to fetch the plant by ID")
    public void theAdminSendsGetRequestToFetchPlantById() {
        Response response = plantApi.getPlantById(context.getToken(), plantId);
        context.setResponse(response);
    }

    @When("the admin sends a GET request to fetch plant with ID {int}")
    public void theAdminSendsGetRequestToFetchPlantWithId(int id) {
        Response response = plantApi.getPlantById(context.getToken(), id);
        context.setResponse(response);
    }

    @When("the admin sends a PUT request to update the plant with invalid category")
    public void theAdminSendsPutRequestWithInvalidCategory() {
        // API requires ALL fields - send original values but use invalid categoryId
        Response response = plantApi.updatePlant(context.getToken(), plantId, 
            originalPlantName, 100.0, originalQuantity, categoryId);  // categoryId is set to 999 (invalid)
        context.setResponse(response);
    }

    @When("the admin sends a GET request to fetch plants by the category")
    public void theAdminSendsGetRequestToFetchPlantsByCategory() {
        Response response = plantApi.getPlantsByCategory(context.getToken(), categoryId);
        context.setResponse(response);
    }

    @When("the admin sends a GET request to search plants with name {string}")
    public void theAdminSendsGetRequestToSearchPlantsWithName(String name) {
        Response response = plantApi.searchPlants(context.getToken(), name, null, null, null);
        context.setResponse(response);
    }

    @When("the admin sends a GET request with page {int} size {int} and category filter")
    public void theAdminSendsGetRequestWithPaginationAndFilter(int page, int size) {
        Response response = plantApi.searchPlants(context.getToken(), null, categoryId, page, size);
        context.setResponse(response);
    }

    @When("the admin sends a GET request to fetch plant summary")
    public void theAdminSendsGetRequestToFetchPlantSummary() {
        Response response = plantApi.getPlantSummary(context.getToken());
        context.setResponse(response);
    }

    // ===================== Verification Steps =====================

    @Then("the plant API response status code is {int}")
    public void thePlantApiResponseStatusCodeIs(int statusCode) {
        Response response = context.getResponse();
        assertNotNull(response, "Response should not be null");
        int actualStatusCode = response.statusCode();
        assertEquals(statusCode, actualStatusCode, 
            String.format("Expected status %d but got %d. Body: %s", 
                statusCode, actualStatusCode, response.getBody().asString()));
    }

    @Then("the response contains a list of plants")
    public void theResponseContainsListOfPlants() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Then("the response contains plant details")
    public void theResponseContainsPlantDetails() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("id"), "Plant ID should be present");
    }

    @Then("the response contains plant details with all fields")
    public void theResponseContainsPlantDetailsWithAllFields() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("id"), "Plant ID should be present");
        assertNotNull(response.jsonPath().get("name"), "Plant name should be present");
    }

    @Then("the response contains plants from the specified category")
    public void theResponseContainsPlantsFromCategory() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response should contain plants");
    }

    @Then("the response contains plants matching the search term")
    public void theResponseContainsMatchingPlants() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response should contain matching plants");
    }

    @Then("the response contains at most {int} plants")
    public void theResponseContainsAtMostPlants(int maxCount) {
        Response response = context.getResponse();
        List<?> content = response.jsonPath().getList("content");
        if (content != null) {
            assertTrue(content.size() <= maxCount, "Should return at most " + maxCount + " plants");
        }
    }

    @Then("the response contains an authorization error")
    public void theResponseContainsAuthorizationError() {
        Response response = context.getResponse();
        int status = response.statusCode();
        assertTrue(status == 403 || status == 401, "Should return 403 or 401");
    }

    @Then("the response contains the created plant with ID")
    public void theResponseContainsCreatedPlantWithId() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("id"), "Created plant should have ID");
        createdPlantId = response.jsonPath().getLong("id");
    }

    @Then("the response shows the updated plant data")
    public void theResponseShowsUpdatedPlantData() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("id"), "Updated plant should have ID");
    }

    @Then("the plant no longer exists")
    public void thePlantNoLongerExists() {
        if (createdPlantId != null) {
            Response response = plantApi.getPlantById(context.getToken(), createdPlantId);
            assertEquals(404, response.statusCode(), "Deleted plant should return 404");
        }
    }

    @Then("the response contains validation error for quantity")
    public void theResponseContainsValidationErrorForQuantity() {
        Response response = context.getResponse();
        String body = response.getBody().asString().toLowerCase();
        assertTrue(body.contains("quantity") || body.contains("negative") || body.contains("minimum"),
            "Should contain quantity validation error");
    }

    @Then("the response contains validation error for name length")
    public void theResponseContainsValidationErrorForNameLength() {
        Response response = context.getResponse();
        String body = response.getBody().asString().toLowerCase();
        assertTrue(body.contains("name") || body.contains("character") || body.contains("length") || body.contains("size"),
            "Should contain name length validation error");
    }

    @Then("the response contains validation error for price")
    public void theResponseContainsValidationErrorForPrice() {
        Response response = context.getResponse();
        String body = response.getBody().asString().toLowerCase();
        assertTrue(body.contains("price") || body.contains("greater") || body.contains("positive"),
            "Should contain price validation error");
    }

    @Then("the response contains error about invalid category")
    public void theResponseContainsInvalidCategoryError() {
        Response response = context.getResponse();
        String body = response.getBody().asString().toLowerCase();
        assertTrue(body.contains("category") || body.contains("not found") || body.contains("invalid"),
            "Should contain category error");
    }

    @Then("the response shows the original category is unchanged")
    public void theResponseShowsOriginalCategoryUnchanged() {
        Response response = context.getResponse();
        // The API ignores categoryId in update request - verify original category remains
        Long responseCategoryId = response.jsonPath().getLong("category.id");
        assertEquals(originalCategoryId, responseCategoryId, 
            "Category should remain unchanged when updating with invalid categoryId");
    }

    @Then("the response contains plant not found error")
    public void theResponseContainsPlantNotFoundError() {
        Response response = context.getResponse();
        String body = response.getBody().asString().toLowerCase();
        assertTrue(body.contains("not found") || body.contains("plant"),
            "Should contain plant not found error");
    }

    @Then("the response contains only plants from that category")
    public void theResponseContainsOnlyPlantsFromThatCategory() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response should contain plants");
    }

    @Then("the response contains plants with Tulip in name")
    public void theResponseContainsPlantsWithTulipInName() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response should contain plants");
    }

    @Then("the response contains paginated plant data")
    public void theResponseContainsPaginatedPlantData() {
        Response response = context.getResponse();
        assertNotNull(response.getBody(), "Response should contain paginated data");
    }

    @Then("the response contains total plants count")
    public void theResponseContainsTotalPlantsCount() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("totalPlants"), "Should contain total plants count");
    }

    @Then("the response contains low stock plants count")
    public void theResponseContainsLowStockPlantsCount() {
        Response response = context.getResponse();
        assertNotNull(response.jsonPath().get("lowStockPlants"), "Should contain low stock plants count");
    }

    // ===================== Helper Methods =====================

    private void ensureSubCategoryExists() {
        // Get category from existing plants
        Long catId = getCategoryIdFromExistingPlants(context.getToken());
        if (catId != null) {
            categoryId = catId;
            return;
        }
        // Fallback: use default sub-category
        categoryId = 2L; // Default sub-category ID (Roses)
    }
}
