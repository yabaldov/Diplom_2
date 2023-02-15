package org.example.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.response.UserRegisterResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserLoginTest extends UserTestBase {

    @Test
    @DisplayName("Should Login an Existing User")
    @Description("Тест логина под существующим пользователем")
    public void shouldLoginUserSuccessfully() {
        User user = UserDataProvider.getDefaultUser();
        createUserAndGetAccessToken(user);

        UserCredentials credentials = new UserCredentials(user);

        ValidatableResponse responseLogin = client.login(credentials);
        assertThat("Логин под существующим пользователем, код ответа должен быть 200 OK.", responseLogin.extract().statusCode(), is(SC_OK));

        UserRegisterResponse userLoggedResponse = responseLogin.extract().as(UserRegisterResponse.class);
        assertThat("При успешном логине в ответе возвращается '\"success\": true'.", userLoggedResponse.getSuccess(), is(true));
        assertThat(String.format("Пользователь должен иметь email %s.", user.getEmail()), userLoggedResponse.getUser().getEmail(), is(user.getEmail()));
        assertThat(String.format("Пользователь должен иметь имя %s.", user.getName()), userLoggedResponse.getUser().getName(), is(user.getName()));
        assertThat("Refresh Token не должен быть пустым.", userLoggedResponse.getRefreshToken().isBlank(), is(false));
        assertThat("Access Token не должен быть пустым.", userLoggedResponse.getAccessToken().isBlank(), is(false));
    }

    @Test
    @DisplayName("Should Not Login a User With Wrong Login And Password")
    @Description("Тест логина с неверным логином и паролем")
    public void shouldNotLoginUserWithWrongLoginAndPassword() {
        UserCredentials wrongCredentials = new UserCredentials(UserDataProvider.getUserWithWrongLoginAndPassword());
        ValidatableResponse response = client.login(wrongCredentials);

        assertThat("Логин с неверным логином и паролем, код ответа должен быть 401 UNAUTHORIZED.", response.extract().statusCode(), is(SC_UNAUTHORIZED));
        assertThat("При логине с неверным логином и паролем в теле ответа должно быть '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("При логине с неверным логином и паролем в теле ответа должно быть ожидаемое сообщение об ошибке.",
                response.extract().path("message"),
                is("email or password are incorrect")
        );
    }
}
