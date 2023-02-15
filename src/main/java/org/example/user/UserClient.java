package org.example.user;

import io.restassured.response.ValidatableResponse;
import org.example.ClientBase;

import java.net.URI;

import static io.restassured.RestAssured.given;

public class UserClient extends ClientBase {

    private static final String USER_REGISTER_PATH = "/api/auth/register";
    private static final String USER_DELETE_PATH = "/api/auth/user";
    private static final String USER_LOGIN_PATH = "/api/auth/login";
    private static final String USER_UPDATE_PATH = "/api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(USER_REGISTER_PATH)
                .then();
    }

    public ValidatableResponse delete(String userBearerToken) {
        return given()
                .spec(getSpec().auth().oauth2(userBearerToken))
                .when()
                .delete(USER_DELETE_PATH)
                .then();
    }

    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(USER_LOGIN_PATH)
                .then();
    }

    public ValidatableResponse update(UserPersonalData data, String userBearerToken) {
        return given()
                .spec(getSpec().auth().oauth2(userBearerToken))
                .body(data)
                .when()
                .patch(USER_UPDATE_PATH)
                .then();
    }
}
