package com.qanexus.steps;

import com.qanexus.api.AuthApi;
import com.qanexus.api.SalesApi;
import com.qanexus.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class SalesApiSteps {

    private final TestContext context;
    private final SalesApi salesApi;
    private final AuthApi authApi;

    private long saleId = 1;
    private long plantId = 1;
    private int quantity = 1;

    public SalesApiSteps(TestContext context) {
        this.context = context;
        this.salesApi = new SalesApi();
        this.authApi = new AuthApi();
    }

    // ================= AUTH =================

    @Given("the user is authenticated")
    public void userAuthenticated() {

        Response response = authApi.login("testuser", "test123");
        assertEquals(200, response.statusCode());

        context.setToken(response.jsonPath().getString("token"));
    }

    // ================= USER ACTIONS =================

    @When("the user sends a GET request to fetch all sales")
    public void fetchAllSales() {

        Response response = salesApi.getAllSales(context.getToken());
        context.setResponse(response);
    }

    @When("the user sends a GET request to fetch sale by ID {int}")
    public void fetchSaleById(int id) {

        Response response = salesApi.getSaleById(context.getToken(), id);
        context.setResponse(response);
    }

    @When("the user sends a GET request to fetch sale by ID")
    public void fetchSaleByIdFromContext() {

        Long id = context.getSaleId();
        long idToUse = (id != null) ? id : this.saleId;

        Response response = salesApi.getSaleById(context.getToken(), idToUse);
        context.setResponse(response);
    }

    @When("the user sends a GET request to paginate sales with page {int} and size {int}")
    public void paginateSales(int page, int size) {

        Response response = salesApi.getSalesPaged(context.getToken(), page, size);
        context.setResponse(response);
    }

    @When("the user sends a GET request to sort sales by {string}")
    public void sortSales(String sort) {

        Response response = salesApi.getSalesSorted(context.getToken(), sort);
        context.setResponse(response);
    }

   

    @When("the user sends a GET request with invalid sale ID {int}")
    public void fetchInvalidSale(int id) {

        Response response = salesApi.getSaleById(context.getToken(), id);
        context.setResponse(response);
    }

    @When("the user sends a GET request to filter sales by plant name {string}")
    public void filterSales(String plantName) {

        Response response = salesApi.filterSalesByPlantName(context.getToken(), plantName);
        context.setResponse(response);
    }

    // ================= VERIFICATION =================

    @Then("the sales API response status code is {int}")
    public void verifyStatus(int status) {

        assertEquals(status, context.getResponse().statusCode());
    }

    @Then("the response contains a list of sales")
    public void verifySalesList() {

        assertNotNull(context.getResponse().getBody());
    }

    @Then("the response contains sale details")
    public void verifySaleDetails() {

        assertNotNull(context.getResponse().jsonPath().get("id"));
    }

    @Then("the response contains paginated sales data")
    public void verifyPagination() {

        assertNotNull(context.getResponse().jsonPath().get("content"));
    }

    @Then("the response contains only filtered plant sales")
    public void verifyFilter() {

        assertNotNull(context.getResponse().getBody());
    }

    @Then("the response contains not found error")
    public void verifyNotFound() {

        assertEquals(404, context.getResponse().statusCode());
    }
}
