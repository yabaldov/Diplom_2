package org.example.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.response.OrderResponse;
import org.example.order.response.OrdersListResponse;
import org.example.user.UserDataProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrdersGetListTest extends OrderTestBase {

    private String randomUserAccessToken;

    private static final String EMPTY_LIST_AS_STRING = "[]";

    @Before
    public void setUp() {
        super.setUp();
        randomUserAccessToken = createUserAndReturnAccessToken(UserDataProvider.getUserWithRandomCredentials());
    }

    @After
    public void tearDown() {
        super.tearDown();
        deleteUser(randomUserAccessToken);
    }

    @Test
    @DisplayName("Should Get an Orders List Of an Authorized User")
    @Description("Получение списка заказов авторизованного пользователя")
    public void shouldGetOrdersListOfAuthUser() {
        ValidatableResponse existingOrdersResponse = client.ordersList(userAccessToken);
        assertThat("Код ответа при получении начального списка заказов должен быть 200 OK.", existingOrdersResponse.extract().statusCode(), is(SC_OK));
        int numberOfExistingOrders = existingOrdersResponse.extract().as(OrdersListResponse.class).getOrders().size();

        List<String> newOrdersIds = new ArrayList<>();
        for (Order order: OrderDataProvider.getDefaultListOfOrders()) {
            ValidatableResponse createOrderResponse = client.create(order, userAccessToken);
            assertThat("Код ответа при создании заказа должен быть 200 OK.", createOrderResponse.extract().statusCode(), is(SC_OK));
            OrderResponse newOrderInfo = createOrderResponse.extract().as(OrderResponse.class);
            newOrdersIds.add(newOrderInfo.getOrder().getId());
        }

        ValidatableResponse updatedOrdersListResponse = client.ordersList(userAccessToken);

        assertThat("Код ответа при получении списка заказов должен быть 200 OK.", existingOrdersResponse.extract().statusCode(), is(SC_OK));
        OrdersListResponse ordersInfo = updatedOrdersListResponse.extract().as(OrdersListResponse.class);
        int expectedNumberOfOrders = numberOfExistingOrders + OrderDataProvider.getDefaultListOfOrders().size();
        assertThat("Признак успешности ответа должен быть 'true'.", ordersInfo.getSuccess(), is(true));
        assertThat("Число заказов должно быть равно сумме количеств существующих заказов и новых.",
                ordersInfo.getOrders().size(),
                equalTo(expectedNumberOfOrders)
        );
        List<String> allOrdersIds = new ArrayList<>();
        ordersInfo.getOrders().forEach(orderInList -> allOrdersIds.add(orderInList.getId()));
        assertThat("Вновь созданные заказы должны содержаться в итоговом списке.", allOrdersIds.containsAll(newOrdersIds), is(true));
    }

    @Test
    @DisplayName("Should Get an Empty Orders List Of Authorized User Without Orders")
    @Description("Получение списка заказов авторизованного пользователя, не имеющего заказов")
    public void shouldGetEmptyOrdersListOfAuthUserWithoutOrders() {
        ValidatableResponse response = client.ordersList(randomUserAccessToken);

        assertThat("Код ответа должен быть 200 OK.", response.extract().statusCode(), is(SC_OK));
        assertThat("Признак успешности ответа должен быть 'true'.", response.extract().path("success"), is(true));
        assertThat(
                "Для нового пользователя со случайными данными список заказов должен быть пуст.",
                response.extract().path("orders").toString(),
                is(EMPTY_LIST_AS_STRING));
        int total = response.extract().path("total");
        int totalToday = response.extract().path("totalToday");
        assertThat("Общее число заказов должно быть больше или равно 0.", total, is(greaterThanOrEqualTo(0)));
        assertThat("Число заказов за сегодня должно быть больше или равно 0.", total, is(greaterThanOrEqualTo(0)));
        assertThat("Число заказов за сегодня должно быть меньше или равно общему числу заказов.", totalToday <= total, is(true));
    }

    @Test
    @DisplayName("Should Not Get an Orders List Of an Unauthorized User")
    @Description("Получение списка заказов не авторизованного пользователя")
    public void shouldNotGetOrdersListOfUnauthUser() {
        ValidatableResponse response = client.ordersList(EMPTY_ACCESS_TOKEN);

        assertThat("Код ответа должен быть 401 UNAUTHORIZED.", response.extract().statusCode(), is(SC_UNAUTHORIZED));
        assertThat("Признак успешности ответа должен быть 'false'.", response.extract().path("success"), is(false));
        assertThat("В ответе должно быть ожидаемое сообщение.", response.extract().path("message"), is("You should be authorised"));
    }
}
