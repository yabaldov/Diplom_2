package org.example.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.response.UserRegisterResponse;
import org.example.user.response.UserDataResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserInfoUpdateTest extends UserTestBase {

    @Test
    @DisplayName("Should Update Email of an Authorized User")
    @Description("Тест обновления email авторизованного пользователя")
    public void shouldUpdateEmailOfAuthorizedUser() {
        User user = UserDataProvider.getDefaultUser();
        String accessToken = createUserAndGetAccessToken(user);

        UserPersonalData userData = new UserPersonalData(user);
        String newEmail = "new" + userData.getEmail();
        userData.setEmail(newEmail);

        ValidatableResponse response = client.update(userData, accessToken);

        assertThat("При успешном обновлении пользователя код ответа должен быть 200 OK.", response.extract().statusCode(), is(SC_OK));
        UserDataResponse userUpdateResult = response.extract().as(UserDataResponse.class);
        String actualEmail = userUpdateResult.getUser().getEmail();
        String actualName = userUpdateResult.getUser().getName();
        assertThat("При успешном обновлении данных пользователя в ответе возвращается '\"success\": true'.", userUpdateResult.getSuccess(), is(true));
        assertThat(String.format("Email должен быть %s.", newEmail), actualEmail, is(newEmail));
        assertThat(String.format("Имя пользователя должно остаться %s.", userData.getName()), actualName, is(userData.getName()));
    }

    @Test
    @DisplayName("Should Update Name of an Authorized User")
    @Description("Тест обновления имени авторизованного пользователя")
    public void shouldUpdateNameOfAuthorizedUser() {
        User user = UserDataProvider.getDefaultUser();
        String accessToken = createUserAndGetAccessToken(user);

        UserPersonalData userData = new UserPersonalData(user);
        String newName = "new" + userData.getName();
        userData.setName(newName);

        ValidatableResponse response = client.update(userData, accessToken);

        assertThat("При успешном обновлении пользователя код ответа должен быть 200 OK.", response.extract().statusCode(), is(SC_OK));
        UserDataResponse userUpdateResult = response.extract().as(UserDataResponse.class);
        String actualEmail = userUpdateResult.getUser().getEmail();
        String actualName = userUpdateResult.getUser().getName();
        assertThat("При успешном обновлении данных пользователя в ответе возвращается '\"success\": true'.", userUpdateResult.getSuccess(), is(true));
        assertThat(String.format("Имя пользователя должно быть %s.", newName), actualName, is(newName));
        assertThat(String.format("Email пользователя должен остаться прежним %s.", userData.getEmail()), actualEmail, is(userData.getEmail()));
    }

    @Test
    @DisplayName("Should Not Update Name of an Unauthorized User")
    @Description("Тест обновления имени неавторизованного пользователя")
    public void shouldNotUpdateDataOfUnauthorizedUser() {
        User user = UserDataProvider.getDefaultUser();
        String accessToken = createUserAndGetAccessToken(user);

        UserPersonalData userData = new UserPersonalData(user);
        String newName = "new" + userData.getName();
        userData.setName(newName);
        String newEmail = "new" + userData.getEmail();
        userData.setEmail(newEmail);

        ValidatableResponse response = client.update(userData, "");
        assertThat("При обновлении неавторизованного пользователя код ответа должен быть 401 UNAUTHORIZED.", response.extract().statusCode(), is(SC_UNAUTHORIZED));
        assertThat("При обновлении неавторизованного пользователя в ответе должно быть '\"success\": false'.", response.extract().path("success"), is(false));
        assertThat("При обновлении неавторизованного пользователя в ответе должно быть ожидаемое сообщение об ошибке.",
                response.extract().path("message"),
                is("You should be authorised")
        );
    }
}
