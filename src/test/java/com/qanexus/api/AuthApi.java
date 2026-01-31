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

    public Response login(String username, String password) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("username", username);
        payload.put("password", password);

        String endpoint = config.getLoginPath();
        String fullUrl = config.getBaseUrl() + endpoint;

        logger.info("=== LOGIN REQUEST ===");
        logger.info("URL: {}", fullUrl);
        logger.info("Payload: {}", payload.toString());

        Response response = client.request()
                .body(payload.toString())
                .post(endpoint);

        logger.info("=== LOGIN RESPONSE ===");
        logger.info("Status: {}", response.statusCode());

        if (response.statusCode() >= 400) {
            logger.error("Error: {}", response.getBody().asString());
        }

        return response;
    }
}
