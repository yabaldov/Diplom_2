package org.example.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.response.*;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class UserCreateTest extends UserTestBase {

    private User user;

    @Before
    public void setUp() {
        super.setUp();
        user = UserDataProvider.getDefaultUser();
    }

    @Test
    @DisplayName("Should Create a User")
    @Description("Тест создания пользователя")
    public void shouldCreateUserSuccessfully() {
        ValidatableResponse response = client.create(user);

        assertThat(
             "При успешном создании пользователя сейчас приходит код ответа 200 OK.",
             response.extract().statusCode(),
             is(SC_OK)
        );
        UserRegisterResponse userCreatedResponse = response.extract().as(UserRegisterResponse.class);

        assertThat("Access Token должен начинаться со строки \"Bearer\".", userCreatedResponse.getAccessToken(), startsWith("Bearer"));

        userAccessTokensForDelete.add(userCreatedResponse.getAccessTokenWithoutBearerWord());

        assertThat(
             "При успешном создании пользователя в ответе возвращается '\"success\": true'.",
             userCreatedResponse.getSuccess(),
             is(true)
        );
        assertThat(String.format(
             "Пользователь должен иметь email %s.", user.getEmail()),
             userCreatedResponse.getUser().getEmail(),
             is(user.getEmail())
        );
        assertThat(String.format(
                     "Пользователь должен иметь имя %s.", user.getName()),
             userCreatedResponse.getUser().getName(),
             is(user.getName())
        );
        assertThat("Refresh Token не должен быть пустым.", userCreatedResponse.getRefreshToken().isBlank(), is(false));
    }

    @Test
    @DisplayName("Should Not Create a User Having Registered Credentials")
    @Description("Тест создания пользователя создать, который уже зарегистрирован")
    public void shouldNotCreateUserHavingRegisteredCredentials() {
        ValidatableResponse response = client.create(user);
        assertThat("Код ответа должен быть 200 OK.", response.extract().statusCode(), is(SC_OK));
        userAccessTokensForDelete.add(response.extract().as(UserRegisterResponse.class).getAccessTokenWithoutBearerWord());

        ValidatableResponse responseOneMoreUser = client.create(user);
        if(responseOneMoreUser.extract().statusCode() == SC_OK) {
            userAccessTokensForDelete.add(responseOneMoreUser.extract().as(UserRegisterResponse.class).getAccessTokenWithoutBearerWord());
        }
        assertThat(
                "При создании уже зарегистрированного пользователя код ответа должен быть 403 FORBIDDEN.",
                responseOneMoreUser.extract().statusCode(),
                is(SC_FORBIDDEN)
        );
        assertThat("В теле ответа должно быть '\"success\": false'.", responseOneMoreUser.extract().path("success"), is(false));
        assertThat("В теле ответа должно быть сообщение, что пользователь уже существует.",
                responseOneMoreUser.extract().path("message"),
                is("User already exists")
        );
    }
}
