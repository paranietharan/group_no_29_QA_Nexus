package com.qanexus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qanexus.config.EnvConfig;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthApi {
    private static final Logger logger = LoggerFactory.getLogger(AuthApi.class);
    private final ApiClient client = new ApiClient();
    private final EnvConfig config = EnvConfig.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public Response loginAdmin(String username, String password) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("username", username);
        payload.put("password", password);

        String endpoint = config.getAdminLoginPath();
        String fullUrl = config.getBaseUrl() + endpoint;

        logger.info("=== ADMIN LOGIN REQUEST ===");
        logger.info("Full URL: {}", fullUrl);
        logger.info("Endpoint Path: {}", endpoint);
        logger.info("Base URL: {}", config.getBaseUrl());
        logger.info("Request Payload: {}", payload.toString());

        Response response = client.request()
                .body(payload.toString())
                .post(endpoint);

        logger.info("=== ADMIN LOGIN RESPONSE ===");
        logger.info("Status Code: {}", response.statusCode());
        logger.info("Status Line: {}", response.statusLine());

        if (response.statusCode() >= 400) {
            logger.error("ERROR Response Body: {}", response.getBody().asString());
            logger.error("Content-Type: {}", response.getContentType());
            if (response.statusCode() == 500 && response.getBody().asString().contains("static resource")) {
                logger.warn("*** ISSUE: Server treating request as static resource ***");
                logger.warn("*** This usually means the endpoint path is incorrect ***");
                logger.warn("*** Please verify the API endpoint with your backend team ***");
            }
        } else {
            logger.debug("Success Response Body: {}", response.getBody().asString());
        }

        return response;
    }
}
