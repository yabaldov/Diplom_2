package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.ClientBase;

import static io.restassured.RestAssured.given;

public class OrderClient extends ClientBase {

    private static final String ORDER_CREATE_PATH = "/api/orders";
    private static final String ORDERS_LIST_PATH = "/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getSpec().auth().oauth2(accessToken))
                .body(order)
                .when()
                .post(ORDER_CREATE_PATH)
                .then();
    }

    @Step("Получение списка заказов пользователя")
    public ValidatableResponse ordersList(String accessToken) {
        return given()
                .spec(getSpec().auth().oauth2(accessToken))
                .when()
                .get(ORDERS_LIST_PATH)
                .then();
    }
}
