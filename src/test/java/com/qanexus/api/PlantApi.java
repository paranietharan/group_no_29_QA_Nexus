package com.qanexus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qanexus.config.EnvConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PlantApi {
    private final ApiClient client = new ApiClient();
    private final EnvConfig config = EnvConfig.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    public Response getAllPlants(String token) {
        return authorizedRequest(token).get("/api/plants");
    }

    public Response getPlantById(String token, long plantId) {
        return authorizedRequest(token).get("/api/plants/" + plantId);
    }

    public Response getPlantsByCategory(String token, long categoryId) {
        return authorizedRequest(token).get("/api/plants/category/" + categoryId);
    }

    public Response searchPlants(String token, String name, Long categoryId, Integer page, Integer size) {
        RequestSpecification req = authorizedRequest(token);
        if (name != null) req.queryParam("name", name);
        if (categoryId != null) req.queryParam("categoryId", categoryId);
        if (page != null) req.queryParam("page", page);
        if (size != null) req.queryParam("size", size);
        return req.get("/api/plants/paged");
    }

    public Response getPlantSummary(String token) {
        return authorizedRequest(token).get("/api/plants/summary");
    }

    public Response createPlant(String token, long categoryId, String name, double price, int quantity) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("name", name);
        payload.put("price", price);
        payload.put("quantity", quantity);
        return authorizedRequest(token)
                .body(payload.toString())
                .post("/api/plants/category/" + categoryId);
    }

    public Response createPlantWithBody(String token, long categoryId, String body) {
        return authorizedRequest(token)
                .body(body)
                .post("/api/plants/category/" + categoryId);
    }

    public Response updatePlant(String token, long plantId, String name, Double price, Integer quantity, Long categoryId) {
        ObjectNode payload = mapper.createObjectNode();
        if (name != null) payload.put("name", name);
        if (price != null) payload.put("price", price);
        if (quantity != null) payload.put("quantity", quantity);
        if (categoryId != null) payload.put("categoryId", categoryId);
        return authorizedRequest(token)
                .body(payload.toString())
                .put("/api/plants/" + plantId);
    }

    public Response deletePlant(String token, long plantId) {
        return authorizedRequest(token).delete("/api/plants/" + plantId);
    }

    private RequestSpecification authorizedRequest(String token) {
        return client.request().header("Authorization", "Bearer " + token);
    }
}
