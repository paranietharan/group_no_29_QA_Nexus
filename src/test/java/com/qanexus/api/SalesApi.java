package com.qanexus.api;

import com.qanexus.config.EnvConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SalesApi {

    private final ApiClient client = new ApiClient();
    private final EnvConfig config = EnvConfig.getInstance();

    public Response getAllSales(String token) {
        return authorizedRequest(token).get("/api/sales");
    }

    public Response getSaleById(String token, long saleId) {
        return authorizedRequest(token).get("/api/sales/" + saleId);
    }
   

    public Response getSalesPaged(String token, int page, int size) {
        return authorizedRequest(token)
                .queryParam("page", page)
                .queryParam("size", size)
                .get("/api/sales/page");
    }

    public Response getSalesSorted(String token, String sort) {
        return authorizedRequest(token)
                .queryParam("sort", sort)
                .get("/api/sales/page");
    }

    public Response filterSalesByPlantName(String token, String plantName) {
        return authorizedRequest(token)
                .queryParam("plantName", plantName)
                .get("/api/sales");
    }

    public Response createSale(String token, long plantId, int quantity) {
        return authorizedRequest(token)
                .queryParam("quantity", quantity)
                .post("/api/sales/plant/" + plantId);
    }

    public Response deleteSale(String token, long saleId) {
        return authorizedRequest(token)
                .delete("/api/sales/" + saleId);
    }

    private RequestSpecification authorizedRequest(String token) {
        return client.request().header("Authorization", "Bearer " + token);
    }
    public Response getSalesPageSorted(String token, String sort) {
    return authorizedRequest(token)
            .queryParam("sort", sort)
            .get("/api/sales/page");
}

}
