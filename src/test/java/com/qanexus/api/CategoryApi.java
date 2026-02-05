package com.qanexus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CategoryApi {
    private final ApiClient client = new ApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Response getAllCategories(String token) {
        return authorizedRequest(token).get("/api/categories");
    }

    public Response getCategoryById(String token, long categoryId) {
        return authorizedRequest(token).get("/api/categories/" + categoryId);
    }

    public Response getMainCategories(String token) {
        return authorizedRequest(token).get("/api/categories/main");
    }

    public Response getSubCategories(String token) {
        return authorizedRequest(token).get("/api/categories/sub-categories");
    }

    public Response getCategorySummary(String token) {
        return authorizedRequest(token).get("/api/categories/summary");
    }

    public Response createCategory(String token, String name, Long parentId) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("name", name);
        if (parentId != null) {
            ObjectNode parent = mapper.createObjectNode();
            parent.put("id", parentId);
            payload.set("parent", parent);
        }
        return authorizedRequest(token)
                .body(payload.toString())
                .post("/api/categories");
    }

    public Response updateCategory(String token, long categoryId, String name, Long parentId) {
        ObjectNode payload = mapper.createObjectNode();
        if (name != null) payload.put("name", name);
        if (parentId != null) payload.put("parentId", parentId);
        return authorizedRequest(token)
                .body(payload.toString())
                .put("/api/categories/" + categoryId);
    }

    public Response deleteCategory(String token, long categoryId) {
        return authorizedRequest(token).delete("/api/categories/" + categoryId);
    }

    private RequestSpecification authorizedRequest(String token) {
        return client.request().header("Authorization", "Bearer " + token);
    }
}
