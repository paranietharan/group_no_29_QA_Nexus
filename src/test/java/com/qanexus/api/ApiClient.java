package com.qanexus.api;

import com.qanexus.config.EnvConfig;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiClient {
    private final EnvConfig config = EnvConfig.getInstance();

    public RequestSpecification request() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }

    // GET request without authentication
    public Response get(String endpoint) {
        return request().get(endpoint);
    }

    // GET request with authentication
    public Response getWithAuth(String endpoint, String token) {
        return request()
                .header("Authorization", "Bearer " + token)
                .get(endpoint);
    }

    // GET request with authentication and query parameters
    public Response getWithAuth(String endpoint, String token, Map<String, String> queryParams) {
        RequestSpecification req = request()
                .header("Authorization", "Bearer " + token);
        
        if (queryParams != null && !queryParams.isEmpty()) {
            req.queryParams(queryParams);
        }
        
        return req.get(endpoint);
    }

    // POST request without authentication
    public Response post(String endpoint, Object body) {
        return request()
                .body(body)
                .post(endpoint);
    }

    // POST request with authentication
    public Response postWithAuth(String endpoint, String token, Object body) {
        return request()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post(endpoint);
    }

    // PUT request with authentication
    public Response putWithAuth(String endpoint, String token, Object body) {
        return request()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .put(endpoint);
    }

    // DELETE request with authentication
    public Response deleteWithAuth(String endpoint, String token) {
        return request()
                .header("Authorization", "Bearer " + token)
                .delete(endpoint);
    }

    // PATCH request with authentication
    public Response patchWithAuth(String endpoint, String token, Object body) {
        return request()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .patch(endpoint);
    }

    // getWithAuth with path parameters
    public Response getWithAuthPathParams(String endpoint, String token, Map<String, String> pathParams) {
        RequestSpecification req = request()
                .header("Authorization", "Bearer " + token);
        
        if (pathParams != null && !pathParams.isEmpty()) {
            req.pathParams(pathParams);
        }
        
        return req.get(endpoint);
    }
}