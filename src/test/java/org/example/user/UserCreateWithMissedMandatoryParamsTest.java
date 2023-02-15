package org.example.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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

    private User user;

    public UserCreateWithMissedMandatoryParamsTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] getQuestionsAndAnswers() {
        return new Object[][] {
                {UserDataProvider.getUserWithoutEmail()},
                {UserDataProvider.getUserWithEmptyEmail()},
                {UserDataProvider.getUserWithoutPassword()},
                {UserDataProvider.getUserWithEmptyPassword()},
                {UserDataProvider.getUserWithoutName()},
                {UserDataProvider.getUserWithEmptyName()},
        };
    }

    @Test
    @DisplayName("Should Not Create a User Without a Mandatory Parameter")
    @Description("Тест невозможности создания пользователя без одного из обязательных полей")
    public void shouldNotCreateUserWithoutMandatoryParam() {
        ValidatableResponse response = client.create(user);

        if(response.extract().statusCode() == SC_OK) {
            userAccessTokensForDelete.add(response.extract().as(UserRegisterResponse.class).getAccessTokenWithoutBearerWord());
        }
        assertThat(
                "При попытке создания пользователя код ответа должен быть 403 FORBIDDEN.",
                response.extract().statusCode(),
                is(SC_FORBIDDEN)
        );
        assertThat("При попытке создания пользователя в теле ответа должно быть '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("При попытке создания пользователя в теле ответа должно быть сообщение, какие входные параметры обязательны.",
                response.extract().path("message"),
                is("Email, password and name are required fields")
        );
    }
}
