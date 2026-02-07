package com.qanexus.steps;

import com.qanexus.api.AuthApi;
import com.qanexus.support.TestContext;
import com.qanexus.support.TestDataLoader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthLoginSteps {
    private final TestContext context;
    private final AuthApi authApi;

    public AuthLoginSteps(TestContext context) {
        this.context = context;
        this.authApi = new AuthApi();
    }

    @Given("the authentication service is available")
    public void theAuthenticationServiceIsAvailable() {
        assertNotNull(authApi, "AuthApi should be initialized");
    }

    @Given("valid admin credentials from {string}")
    public void validAdminCredentialsFrom(String resourcePath) {
        Map<String, String> data = TestDataLoader.loadJsonMap(resourcePath);
        context.setUsername(data.get("username"));
        context.setPassword(data.get("password"));
    }

    @Given("valid user credentials from {string}")
    public void validUserCredentialsFrom(String resourcePath) {
        Map<String, String> data = TestDataLoader.loadJsonMap(resourcePath);
        context.setUsername(data.get("username"));
        context.setPassword(data.get("password"));
    }

    @Given("admin username with invalid password")
    public void adminUsernameWithInvalidPassword() {
        context.setUsername("admin");
        context.setPassword("wrongpassword123");
    }

    @Given("user username with invalid password")
    public void userUsernameWithInvalidPassword() {
        context.setUsername("testuser");
        context.setPassword("wrongpassword123");
    }

    @Given("non-existent admin credentials")
    public void nonExistentAdminCredentials() {
        context.setUsername("fakeadmin@example.com");
        context.setPassword("password123");
    }

    @Given("non-existent user credentials")
    public void nonExistentUserCredentials() {
        context.setUsername("fakeuser@example.com");
        context.setPassword("password123");
    }

    @Given("empty username and password")
    public void emptyUsernameAndPassword() {
        context.setUsername("");
        context.setPassword("");
    }

    @Given("empty username with valid admin password")
    public void emptyUsernameWithValidAdminPassword() {
        context.setUsername("");
        context.setPassword("admin123");
    }

    @Given("valid admin username with empty password")
    public void validAdminUsernameWithEmptyPassword() {
        context.setUsername("admin");
        context.setPassword("");
    }

    @Given("empty username with valid user password")
    public void emptyUsernameWithValidUserPassword() {
        context.setUsername("");
        context.setPassword("test123");
    }

    @Given("valid user username with empty password")
    public void validUserUsernameWithEmptyPassword() {
        context.setUsername("testuser");
        context.setPassword("");
    }

    @Given("SQL injection payload in credentials")
    public void sqlInjectionPayloadInCredentials() {
        context.setUsername("admin' OR '1'='1");
        context.setPassword("' OR '1'='1");
    }

    @Given("SQL injection payload in username")
    public void sqlInjectionPayloadInUsername() {
        context.setUsername("admin' OR '1'='1' --");
        context.setPassword("admin123");
    }

    @Given("SQL injection payload in password")
    public void sqlInjectionPayloadInPassword() {
        context.setUsername("admin");
        context.setPassword("' OR '1'='1' --");
    }

    @Given("XSS payload in credentials")
    public void xssPayloadInCredentials() {
        context.setUsername("<script>alert('xss')</script>");
        context.setPassword("<script>alert('xss')</script>");
    }

    @When("the login request is sent")
    public void theLoginRequestIsSent() {
        Response response = authApi.login(context.getUsername(), context.getPassword());
        context.setResponse(response);
    }

    @Then("the response status code is {int}")
    public void theResponseStatusCodeIs(int statusCode) {
        Response response = context.getResponse();
        int actualStatusCode = response.statusCode();
        if (actualStatusCode != statusCode) {
            String errorMessage = String.format(
                "Expected status code %d but got %d. Response body: %s",
                statusCode, actualStatusCode, response.getBody().asString()
            );
            assertEquals(statusCode, actualStatusCode, errorMessage);
        } else {
            assertEquals(statusCode, actualStatusCode);
        }
    }

    @Then("the response contains a non-empty token")
    public void theResponseContainsANonEmptyToken() {
        String token = context.getResponse().jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isBlank(), "Token should not be blank");
        context.setToken(token);
    }

    @Then("the token should be a valid JWT format")
    public void theTokenShouldBeAValidJWTFormat() {
        String token = context.getToken();
        assertNotNull(token, "Token should exist");
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT token should have 3 parts separated by dots, got: " + token);
        for (int i = 0; i < parts.length; i++) {
            assertFalse(parts[i].isBlank(), "JWT part " + i + " should not be empty");
        }
    }

    @Then("the response contains an error message")
    public void theResponseContainsAnErrorMessage() {
        String errorMessage = context.getResponse().jsonPath().getString("message");
        assertNotNull(errorMessage, "Error message should not be null");
        assertFalse(errorMessage.isBlank(), "Error message should not be blank");
    }

    @Then("the response does not contain error details")
    public void theResponseDoesNotContainErrorDetails() {
        String responseBody = context.getResponse().getBody().asString();
        assertFalse(responseBody.contains("stack trace"), "Response should not contain stack trace");
        assertFalse(responseBody.toLowerCase().contains("database"), "Response should not contain database details");
        assertFalse(responseBody.toLowerCase().contains("exception"), "Response should not contain exception details");
    }

    @Then("the response does not contain script tags")
    public void theResponseDoesNotContainScriptTags() {
        String responseBody = context.getResponse().getBody().asString();
        assertFalse(responseBody.toLowerCase().contains("<script>"), "Response should not contain script tags");
        assertFalse(responseBody.toLowerCase().contains("</script>"), "Response should not contain script closing tags");
    }
}
