package org.example.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.response.Ingredient;
import org.example.order.response.OrderPlaced;
import org.example.order.response.OrderResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderCreateTest extends OrderTestBase {

    private static final List<String> VALID_INGREDIENTS = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa71");
    private static final List<String> EMPTY_INGREDIENTS = new ArrayList<>();
    private static final List<String> INCORRECT_INGREDIENTS = Arrays.asList("63e672809ed280001b2d5ea9", "63e671429ed280001b2d5ea0");
    private static final List<String> INVALID_INGREDIENTS = Arrays.asList("Флюоресцентная булка R2-D3", "Говяжий метеорит (отбивная)");

    @Test
    @DisplayName("Should Create an Order With Correct Ingredients for Authorized User")
    @Description("Создание заказа с авторизацией и корректными ингредиентами")
    public void shouldCreateOrderWithCorrectIngredientsForAuthUser() {
        Order order = new Order(VALID_INGREDIENTS);
        int expectedIngredientsQuantity = order.getIngredients().size();

        ValidatableResponse response = orderClient.create(order, randomUserAccessToken);

        assertThat("При успешном создании заказа сейчас приходит код ответа 200 OK.", response.extract().statusCode(), is(SC_OK));
        OrderResponse orderInfo = response.extract().as(OrderResponse.class);
        assertThat("При успешном создании заказа в ответе возвращается '\"success\": true'.", orderInfo.getSuccess(), is(true));
        assertThat("Наименование заказа не должно быть пустым.", orderInfo.getName(), notNullValue());
        OrderPlaced orderPlaced = orderInfo.getOrder();
        assertThat("Заказ должен содержать описание заказа.", orderPlaced, notNullValue());
        List<Ingredient> ingredients = orderPlaced.getIngredients();
        assertThat("Заказ должен содержать список ингредиентов.", ingredients, notNullValue());
        assertThat("Размер списка ингредиентов должен совпадать с исходным их количеством.", ingredients.size(), is(expectedIngredientsQuantity));
        for (String item: order.getIngredients()) {
            assertThat("Список ингредиентов в ответе должен совпадать со списком из запроса. ", orderPlaced.getListOfIngredientsIds(), hasItem(item));
        }
        assertThat("У заказа должен быть номер.", orderPlaced.getNumber(), notNullValue());
        assertThat("Номер заказа должен быть больше 0.", orderPlaced.getNumber(), greaterThan(0));
    }

    @Test
    @DisplayName("Should Create an Order for Unauthorized User")
    @Description("Создание заказа без авторизации и корректными ингредиентами")
    public void shouldCreateOrderWithCorrectIngredientsForUnauthUser() {
        Order order = new Order(VALID_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, EMPTY_ACCESS_TOKEN);

        assertThat("При создании заказа сейчас приходит код ответа 200 OK.", response.extract().statusCode(), is(SC_OK));
        assertThat("При создании заказа в ответе возвращается '\"success\": true'.", response.extract().path("success"), is(true));
        assertThat("Наименование заказа не должно быть пустым.", response.extract().path("name"), notNullValue());
        assertThat("У заказа должен быть номер.", response.extract().path("order.number"), notNullValue());
        assertThat("Номер заказа должен быть больше 0.", response.extract().path("order.number"), greaterThan(0));
        assertThat("Список ингредиентов в заказе должен отсутствовать.", response.extract().path("order.ingredients"), is(nullValue()));
        assertThat("Владелец заказа должен отсутствовать.", response.extract().path("owner"), is(nullValue()));
    }

    @Test
    @DisplayName("Should Not Create an Order Without Ingredients for Authorized User")
    @Description("Создание заказа с авторизацией без ингредиентов.")
    public void shouldNotCreateOrderWithoutIngredientsForAuthUser() {
        Order order = new Order(EMPTY_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, randomUserAccessToken);

        assertThat("При создании заказа без ингредиентов приходит код ответа 400 BAD REQUEST.", response.extract().statusCode(), is(SC_BAD_REQUEST));
        assertThat("При создании заказа без ингредиентов в ответе возвращается '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("Должно быть ожидаемое сообщение об ошибке.", response.extract().path("message"), is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Should Not Create an Order Without Ingredients for Unauthorized User")
    @Description("Создание заказа с без авторизацией без ингредиентов.")
    public void shouldNotCreateOrderWithoutIngredientsForUnauthUser() {
        Order order = new Order(EMPTY_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, EMPTY_ACCESS_TOKEN);

        assertThat("При создании заказа без ингредиентов приходит код ответа 400 BAD REQUEST.", response.extract().statusCode(), is(SC_BAD_REQUEST));
        assertThat("При создании заказа без ингредиентов в ответе возвращается '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("Должно быть ожидаемое сообщение об ошибке.", response.extract().path("message"), is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Should Not Create an Order With Incorrect Ingredients for Authorized User")
    @Description("Создание заказа с авторизацией с некорректными ингредиентами.")
    public void shouldNotCreateOrderWithIncorrectIngredientsForAuthUser() {
        Order order = new Order(INCORRECT_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, randomUserAccessToken);

        assertThat("При заказе с некорректными ингредиентами приходит код ответа 400 BAD REQUEST.", response.extract().statusCode(), is(SC_BAD_REQUEST));
        assertThat("При заказе с некорректными ингредиентами в ответе возвращается '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("Должно быть ожидаемое сообщение об ошибке.", response.extract().path("message"), is("One or more ids provided are incorrect"));
    }

    @Test
    @DisplayName("Should Not Create an Order With Incorrect Ingredients for Unauthorized User")
    @Description("Создание заказа с без авторизацией с некорректными ингредиентами.")
    public void shouldNotCreateOrderWithIncorrectForUnauthUser() {
        Order order = new Order(INCORRECT_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, EMPTY_ACCESS_TOKEN);

        assertThat("При создании заказа некорректными ингредиентами приходит код ответа 400 BAD REQUEST.", response.extract().statusCode(), is(SC_BAD_REQUEST));
        assertThat("При создании заказа некорректными ингредиентами в ответе возвращается '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("Должно быть ожидаемое сообщение об ошибке.", response.extract().path("message"), is("One or more ids provided are incorrect"));
    }

    @Test
    @DisplayName("Should Not Create an Order With Invalid Ingredients for Authorized User")
    @Description("Создание заказа с авторизацией с неверным хешем ингредиентов.")
    public void shouldNotCreateOrderWithInvalidIngredientsForAuthUser() {
        Order order = new Order(INVALID_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, randomUserAccessToken);

        assertThat("При заказе с неверным хешем ингредиентов приходит код ответа 500 INTERNAL SERVER ERROR.", response.extract().statusCode(), is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("Should Not Create an Order With Invalid Ingredients for Unauthorized User")
    @Description("Создание заказа с без авторизацией с неверным хешем ингредиентов.")
    public void shouldNotCreateOrderWithInvalidIngredientsForUnauthUser() {
        Order order = new Order(INVALID_INGREDIENTS);

        ValidatableResponse response = orderClient.create(order, EMPTY_ACCESS_TOKEN);

        assertThat("При заказе с неверным хешем ингредиентов приходит код ответа 500 INTERNAL SERVER ERROR.", response.extract().statusCode(), is(SC_INTERNAL_SERVER_ERROR));
    }
}
