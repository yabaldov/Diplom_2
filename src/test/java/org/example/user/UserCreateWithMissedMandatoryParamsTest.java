package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.example.user.response.UserRegisterResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class UserCreateWithMissedMandatoryParamsTest extends UserTestBase {

    private final User user;
    private final String comment;

    public UserCreateWithMissedMandatoryParamsTest(User user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Пользователь: {1}")
    public static Object[][] getInvalidUserWithComment() {
        return new Object[][] {
                {UserDataProvider.getUserWithoutEmail(), "без email"},
                {UserDataProvider.getUserWithEmptyEmail(), "с пустым email"},
                {UserDataProvider.getUserWithoutPassword(), "без пароля"},
                {UserDataProvider.getUserWithEmptyPassword(), "с пустым паролем"},
                {UserDataProvider.getUserWithoutName(), "без имени"},
                {UserDataProvider.getUserWithEmptyName(), "с пустым именем"},
        };
    }

    @Test
    @Description("Тест невозможности создания пользователя без одного из обязательных полей")
    public void shouldNotCreateUserWithoutMandatoryParam() {
        ValidatableResponse response = userClient.create(user);

        if(response.extract().statusCode() == SC_OK) {
            userAccessTokensForDelete.add(response.extract().as(UserRegisterResponse.class).getAccessTokenWithoutBearerWord());
        }
        assertThat(
                String.format("При попытке создания пользователя %s код ответа должен быть 403 FORBIDDEN.", comment),
                response.extract().statusCode(),
                is(SC_FORBIDDEN));
        assertThat(String.format("При попытке создания пользователя %s в теле ответа должно быть '\"success\": false'.", comment),
                response.extract().path("success"),
                is(false));
        assertThat(String.format("При попытке создания пользователя %s в теле ответа должно быть сообщение, какие входные параметры обязательны.", comment),
                response.extract().path("message"),
                is("Email, password and name are required fields"));
    }
}
