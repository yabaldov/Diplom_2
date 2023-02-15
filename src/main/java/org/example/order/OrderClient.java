package org.example.order;

import io.restassured.response.ValidatableResponse;
import org.example.ClientBase;
import org.example.user.User;

import static io.restassured.RestAssured.given;

public class OrderClient extends ClientBase {

    private static final String ORDER_CREATE_PATH = "/api/orders";
    private static final String ORDERS_LIST_PATH = "/api/orders";

    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getSpec().auth().oauth2(accessToken))
                .body(order)
                .when()
                .post(ORDER_CREATE_PATH)
                .then();
    }

    public ValidatableResponse ordersList(String accessToken) {
        return given()
                .spec(getSpec().auth().oauth2(accessToken))
                .when()
                .get(ORDERS_LIST_PATH)
                .then();
    }
}
