package com.qanexus.api;

import com.qanexus.config.EnvConfig;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Plant_Auth_client {
    private final EnvConfig config = EnvConfig.getInstance();

    public RequestSpecification request() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter());
    }
}
