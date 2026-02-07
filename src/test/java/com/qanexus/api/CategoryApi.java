package com.qanexus.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryApi {
    private static final Logger logger = LoggerFactory.getLogger(CategoryApi.class);
    private final ApiClient client = new ApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Response getAllCategories(String token) {
        try {
            return authorizedRequest(token).get("/api/categories");
        } catch (Exception e) {
            logger.error("Exception getting all categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get all categories: " + e.getMessage(), e);
        }
    }

    public Response getCategory(String token, long categoryId) {
        try {
            return authorizedRequest(token).get("/api/categories/" + categoryId);
        } catch (Exception e) {
            logger.error("Exception getting category {}: {}", categoryId, e.getMessage(), e);
            throw new RuntimeException("Failed to get category: " + e.getMessage(), e);
        }
    }

    public Response getMainCategories(String token) {
        try {
            return authorizedRequest(token).get("/api/categories/main");
        } catch (Exception e) {
            logger.error("Exception getting main categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get main categories: " + e.getMessage(), e);
        }
    }

    public Response searchCategories(String token, String searchTerm) {
        try {
            return authorizedRequest(token)
                    .param("search", searchTerm)
                    .get("/api/categories");
        } catch (Exception e) {
            logger.error("Exception searching categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search categories: " + e.getMessage(), e);
        }
    }

    public Response filterCategoriesByParent(String token, Long parentId) {
        try {
            return authorizedRequest(token)
                    .param("parentId", parentId)
                    .get("/api/categories");
        } catch (Exception e) {
            logger.error("Exception filtering categories by parent {}: {}", parentId, e.getMessage(), e);
            throw new RuntimeException("Failed to filter categories: " + e.getMessage(), e);
        }
    }

    public Response getCategoriesPaged(String token, int page, int size) {
        return getCategoriesPaged(token, page, size, null, "asc");
    }

    public Response getCategoriesPaged(String token, int page, int size, String sortBy, String direction) {
        try {
            RequestSpecification req = authorizedRequest(token)
                    .param("page", page)
                    .param("size", size)
                    .param("direction", direction);
            if (sortBy != null) req = req.param("sortBy", sortBy);
            return req.get("/api/categories");
        } catch (Exception e) {
            logger.error("Exception getting paginated categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get paginated categories: " + e.getMessage(), e);
        }
    }

    public Response createCategory(String token, String name, Long parentId) {
        try {
            ObjectNode payload = mapper.createObjectNode();
            payload.put("name", name);
            if (parentId != null) {
                payload.put("parentId", parentId);
            }
            
            logger.info("=== CREATE CATEGORY REQUEST ===");
            logger.info("Name: {}, ParentId: {}", name, parentId);
            logger.info("Payload: {}", payload.toString());
            
            Response response = authorizedRequest(token)
                    .body(payload.toString())
                    .post("/api/categories");
            
            logger.info("=== CREATE CATEGORY RESPONSE ===");
            logger.info("Status: {}", response.statusCode());
            
            if (response.statusCode() >= 400) {
                logger.error("Error: {}", response.getBody().asString());
            }
            
            return response;
        } catch (Exception e) {
            logger.error("Exception creating category: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create category: " + e.getMessage(), e);
        }
    }

    public Response updateCategory(String token, long categoryId, String name, Long parentId) {
        try {
            ObjectNode payload = mapper.createObjectNode();
            if (name != null) payload.put("name", name);
            if (parentId != null) payload.put("parentId", parentId);
            return authorizedRequest(token)
                    .body(payload.toString())
                    .put("/api/categories/" + categoryId);
        } catch (Exception e) {
            logger.error("Exception updating category {}: {}", categoryId, e.getMessage(), e);
            throw new RuntimeException("Failed to update category: " + e.getMessage(), e);
        }
    }

    public Response deleteCategory(String token, long categoryId) {
        try {
            return authorizedRequest(token).delete("/api/categories/" + categoryId);
        } catch (Exception e) {
            logger.error("Exception deleting category {}: {}", categoryId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete category: " + e.getMessage(), e);
        }
    }

    private RequestSpecification authorizedRequest(String token) {
        return client.request().header("Authorization", "Bearer " + token);
    }
}