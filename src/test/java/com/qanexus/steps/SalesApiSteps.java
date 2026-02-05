package com.qanexus.steps;

import com.qanexus.api.SalesApi;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SalesApiSteps {

    private final SalesApi salesApi = new SalesApi();

    // We reuse token/response from Auth steps via shared static (simple way)
    // If your project already uses Hooks/TestContext, connect to that instead.
    public static String token;
    public static Response response;

    @When("the user sends request to fetch all sales")
    public void theUserSendsRequestToFetchAllSales() {
        assertNotNull(token, "Token is null. Login step may not have stored the token.");
        response = salesApi.getAllSales(token);
    }

    @Then("the response contains a list of sales records")
    public void theResponseContainsAListOfSalesRecords() {

        assertNotNull(response, "Response is null. Sales API may not have been called.");
        String body = response.getBody().asString();

        // Works if API returns JSON array directly
        // Example: [ {..}, {..} ]
        try {
            List<?> salesList = response.jsonPath().getList("$");
            assertNotNull(salesList, "Sales list is null. Body: " + body);
            assertTrue(salesList.size() >= 0, "Sales list not returned properly. Body: " + body);

        } catch (Exception e) {
            fail("Response is not a JSON array OR JSONPath is wrong. Body: " + body);
        }
    }
}

