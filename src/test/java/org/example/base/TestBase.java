package org.example.base;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.response.UserRegisterResponse;
import org.junit.Before;

import static org.apache.http.HttpStatus.SC_OK;

public abstract class TestBase {

    protected UserClient userClient;

    protected static final String EMPTY_ACCESS_TOKEN = "";

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Step("Получение токена вновь созданного пользователя")
    protected String createUserAndReturnAccessToken(User user) {
        ValidatableResponse responseCreate = userClient.create(user);
        String accessToken = EMPTY_ACCESS_TOKEN;
        if(responseCreate.extract().statusCode() == SC_OK) {
            accessToken = responseCreate.extract()
                    .as(UserRegisterResponse.class)
                    .getAccessTokenWithoutBearerWord();
        }
        return accessToken;
    }

    @Step("Удаление пользователя по полученному токену.")
    protected void deleteUser(String accessToken) {
        if(!accessToken.isEmpty()) {
            userClient.delete(accessToken);
        }
    }
}
