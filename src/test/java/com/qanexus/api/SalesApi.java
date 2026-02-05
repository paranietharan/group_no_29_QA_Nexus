package com.qanexus.api;

import com.qanexus.config.EnvConfig;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesApi {

    private static final Logger logger = LoggerFactory.getLogger(SalesApi.class);
    private final ApiClient client = new ApiClient();
    private final EnvConfig config = EnvConfig.getInstance();

    public Response getAllSales(String token) {

        // If your EnvConfig doesn't have sales path, hardcode it temporarily:
        // String endpoint = "/api/sales";
        String endpoint = config.getSalesPath(); // add this in EnvConfig (shown below)

        String fullUrl = config.getBaseUrl() + endpoint;

        logger.info("=== GET ALL SALES REQUEST ===");
        logger.info("URL: {}", fullUrl);

        Response response = client.request()
                .header("Authorization", "Bearer " + token)
                .get(endpoint);

        logger.info("=== GET ALL SALES RESPONSE ===");
        logger.info("Status: {}", response.statusCode());

        if (response.statusCode() >= 400) {
            logger.error("Error: {}", response.getBody().asString());
        }

        return response;
    }
}

