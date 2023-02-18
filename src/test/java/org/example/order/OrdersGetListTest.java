package org.example.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.response.OrderInList;
import org.example.order.response.OrderResponse;
import org.example.order.response.OrdersListResponse;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrdersGetListTest extends OrderTestBase {

    @Test
    @DisplayName("Should Get an Empty Orders List For Authorized User Without Orders")
    @Description("Получение списка заказов авторизованного пользователя, не имеющего заказов")
    public void shouldGetEmptyOrdersListForAuthUserWithoutOrders() {

        ValidatableResponse response = orderClient.ordersList(randomUserAccessToken);

        assertThat("Код ответа должен быть 200 OK.", response.extract().statusCode(), is(SC_OK));
        OrdersListResponse ordersInfo = response.extract().as(OrdersListResponse.class);
        assertThat("Признак успешности ответа должен быть 'true'.", ordersInfo.getSuccess(), is(true));
        assertThat(
                "Для нового пользователя со случайными данными список заказов должен быть пуст.",
                ordersInfo.getOrders().size(),
                is(equalTo(0)));
        int total = ordersInfo.getTotal();
        int totalToday = ordersInfo.getTotalToday();
        assertThat("Общее число заказов должно быть больше или равно 0.", total, is(greaterThanOrEqualTo(0)));
        assertThat("Число заказов за сегодня должно быть больше или равно 0.", total, is(greaterThanOrEqualTo(0)));
        assertThat("Число заказов за сегодня должно быть меньше или равно общему числу заказов.", totalToday <= total, is(true));
    }

    @Test
    @DisplayName("Should Get an Orders List Of an Authorized User")
    @Description("Получение списка заказов авторизованного пользователя")
    public void shouldGetOrdersListOfAuthUser() {
        int expectedNumberOfOrders = OrderDataProvider.getDefaultListOfOrders().size();
        List<String> ordersIds = new ArrayList<>();
        for (Order order: OrderDataProvider.getDefaultListOfOrders()) {
            ValidatableResponse createOrderResponse = orderClient.create(order, randomUserAccessToken);
            OrderResponse orderInfo = createOrderResponse.extract().as(OrderResponse.class);
            ordersIds.add(orderInfo.getOrder().getId());
        }

        ValidatableResponse ordersListResponse = orderClient.ordersList(randomUserAccessToken);

        assertThat("Код ответа при получении списка заказов должен быть 200 OK.", ordersListResponse.extract().statusCode(), is(SC_OK));
        OrdersListResponse ordersInfo = ordersListResponse.extract().as(OrdersListResponse.class);
        assertThat("Признак успешности ответа должен быть 'true'.", ordersInfo.getSuccess(), is(true));
        assertThat("Число заказов должно быть равно числу созданных.", ordersInfo.getOrders().size(), equalTo(expectedNumberOfOrders));
        List<OrderInList> ordersList = ordersInfo.getOrders();
        List<String> receivedOrdersIds = new ArrayList<>();
        ordersList.forEach(orderInList -> receivedOrdersIds.add(orderInList.getId()));
        assertThat("Вновь созданные заказы должны содержаться в итоговом списке.", receivedOrdersIds.containsAll(ordersIds), is(true));
    }

    @Test
    @DisplayName("Should Not Get an Orders List Of an Unauthorized User")
    @Description("Получение списка заказов не авторизованного пользователя")
    public void shouldNotGetOrdersListOfUnauthUser() {
        ValidatableResponse response = orderClient.ordersList(EMPTY_ACCESS_TOKEN);

        assertThat("Код ответа должен быть 401 UNAUTHORIZED.", response.extract().statusCode(), is(SC_UNAUTHORIZED));
        assertThat("Признак успешности ответа должен быть 'false'.", response.extract().path("success"), is(false));
        assertThat("В ответе должно быть ожидаемое сообщение.", response.extract().path("message"), is("You should be authorised"));
    }
}
