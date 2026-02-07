package com.qanexus.steps;

import com.qanexus.api.AuthApi;
import com.qanexus.api.SalesApi;
import com.qanexus.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SalesApiAdminSteps {

    private final TestContext context;
    private final AuthApi authApi;
    private final SalesApi salesApi;

    public SalesApiAdminSteps(TestContext context) {
        this.context = context;
        this.authApi = new AuthApi();
        this.salesApi = new SalesApi();
    }

    // ================= AUTH =================

    @Given("the admin is authenticated")
    public void theAdminIsAuthenticated() {
        Response res = authApi.login("admin", "admin123"); // <-- use your real admin creds
        assertEquals(200, res.statusCode(), "Admin login failed");

        String token = res.jsonPath().getString("token");
        assertNotNull(token, "Admin token is null");

        context.setToken(token);
        System.out.println("ADMIN TOKEN: " + token);
    }

    // ================= PRECONDITIONS =================

    @Given("an existing sale record is available")
    public void anExistingSaleRecordIsAvailable() {
        String token = context.getToken();
        assertNotNull(token, "Token is null. Did you run: Given the admin is authenticated ?");

        Response res = salesApi.getAllSales(token);
        assertEquals(200, res.statusCode(), "Failed to fetch sales list for precondition");

        List<Map<String, Object>> sales = res.jsonPath().getList("");
        assertNotNull(sales, "Sales list is null");
        assertFalse(sales.isEmpty(),
                "No sales exist in DB. Add at least one sale in DB (manual/seed) to run delete/fetch-by-id tests.");

        Long saleId = Long.parseLong(sales.get(0).get("id").toString());
        context.setSaleId(saleId);
    }

    @Given("a valid sale exists")
    public void aValidSaleExists() {
        anExistingSaleRecordIsAvailable();
    }

    // ================= ACTIONS (TCs) =================

    // TC_API_SALES_ADMIN_03 Fetch all sales
    @When("the admin sends a GET request to fetch all sales")
    public void adminFetchAllSales() {
        Response res = salesApi.getAllSales(context.getToken());
        context.setResponse(res);
    }

    // TC_API_SALES_ADMIN_04 Fetch sale by ID
    @When("the admin sends a GET request to fetch sale by ID")
    public void adminFetchSaleById() {
        Long saleId = context.getSaleId();
        assertNotNull(saleId, "saleId is null. Did you run: Given an existing sale record is available ?");
        Response res = salesApi.getSaleById(context.getToken(), saleId);
        context.setResponse(res);
    }

    // TC_API_SALES_ADMIN_02 Delete sale
    @When("the admin sends a DELETE request to delete the sale")
    public void adminDeleteSale() {
        Long saleId = context.getSaleId();
        assertNotNull(saleId, "saleId is null. Did you run: Given an existing sale record is available ?");
        Response res = salesApi.deleteSale(context.getToken(), saleId);
        context.setResponse(res);
    }

    // TC_API_SALES_ADMIN_13 Delete non-existent sale
    @When("the admin sends a DELETE request with non-existent sale ID {long}")
    public void adminDeleteNonExistentSale(long invalidId) {
        Response res = salesApi.deleteSale(context.getToken(), invalidId);
        context.setResponse(res);
    }

    // Backwards-compatible step that matches feature text using 'invalid sale ID {int}'
    @When("the admin sends a DELETE request with invalid sale ID {int}")
    public void adminDeleteWithInvalidSaleId(int invalidId) {
        Response res = salesApi.deleteSale(context.getToken(), (long) invalidId);
        context.setResponse(res);
    }

    // TC_API_SALES_ADMIN_14 Paginate sales
    @When("the admin requests paginated sales sorted by soldAt desc")
    public void adminPaginateSalesSortedBySoldAtDesc() {
        Response res = salesApi.getSalesPageSorted(context.getToken(), "soldAt,desc");
        context.setResponse(res);
    }

    // Backwards-compatible admin step matching feature text for pagination
    @When("the admin sends a GET request to paginate sales with page {int} and size {int}")
    public void adminPaginateSales(int page, int size) {
        Response res = salesApi.getSalesPaged(context.getToken(), page, size);
        context.setResponse(res);
    }

    // TC_API_SALES_ADMIN_15 Sort sales (same endpoint usage)
    @When("the admin requests sales sorted by soldAt desc")
    public void adminSortSalesBySoldAtDesc() {
        Response res = salesApi.getSalesPageSorted(context.getToken(), "soldAt,desc");
        context.setResponse(res);
    }

    // Backwards-compatible admin step matching feature text for sorting
    @When("the admin sends a GET request to sort sales by {string}")
    public void adminSortSalesBy(String sort) {
        Response res = salesApi.getSalesSorted(context.getToken(), sort);
        context.setResponse(res);
    }

    // ================= VERIFICATIONS =================

    @Then("the sales API response status code should be {int}")
    public void verifyStatus(int expected) {
        assertNotNull(context.getResponse(), "Response is null");
        assertEquals(expected, context.getResponse().statusCode());
    }

    @Then("a sales list should be returned")
    public void verifySalesListReturned() {
        Response res = context.getResponse();
        assertNotNull(res.getBody(), "Body is null");
        // If GET /api/sales returns array:
        List<Map<String, Object>> sales = res.jsonPath().getList("");
        assertNotNull(sales, "Sales list is null");
    }

    @Then("sale details should be returned")
    public void verifySaleDetails() {
        Response res = context.getResponse();
        assertNotNull(res.jsonPath().get("id"), "id is missing");
        assertNotNull(res.jsonPath().get("quantity"), "quantity is missing");
        // plant may be object or plantId depending on backend:
        assertTrue(res.jsonPath().get("plant") != null || res.jsonPath().get("plantId") != null,
                "plant/plantId is missing");
    }

    @Then("the sale should be deleted successfully")
    public void verifySaleDeleted() {
        Long saleId = context.getSaleId();
        assertNotNull(saleId, "saleId is null");

        // After DELETE 204, verify it's gone (GET should be 404)
        Response check = salesApi.getSaleById(context.getToken(), saleId);
        assertEquals(404, check.statusCode(), "Sale still exists after delete");
    }

    @Then("the sale is deleted successfully")
    public void theSaleIsDeletedSuccessfully() {
        verifySaleDeleted();
    }

    @Then("paginated sales data should be returned")
    public void verifyPaginatedDataReturned() {
        Response res = context.getResponse();
        assertNotNull(res.jsonPath().get("content"), "content missing in pagination response");
    }

    @Then("sales should be sorted by soldAt in descending order")
    public void verifySortedBySoldAtDesc() {
        Response res = context.getResponse();

        // Pagination response assumed: { content: [ ... ] }
        List<Map<String, Object>> content = res.jsonPath().getList("content");
        assertNotNull(content, "content list is null");
        assertTrue(content.size() >= 2, "Need at least 2 sales to validate sorting");

        for (int i = 0; i < content.size() - 1; i++) {
            String current = String.valueOf(content.get(i).get("soldAt"));
            String next = String.valueOf(content.get(i + 1).get("soldAt"));

            // If soldAt is ISO date-time
            Instant curTime = Instant.parse(current);
            Instant nextTime = Instant.parse(next);

            assertTrue(!curTime.isBefore(nextTime),
                    "Sorting wrong at index " + i + ": " + current + " should be >= " + next);
        }
    }
}
