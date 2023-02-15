package org.example.user;

import io.restassured.response.ValidatableResponse;
import org.example.user.response.UserRegisterResponse;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class UserTestBase {

    protected UserClient client;
    protected List<String> userAccessTokensForDelete;

    @Before
    public void setUp() {
        client = new UserClient();
        userAccessTokensForDelete = new ArrayList<String>();
    }

    @After
    public void tearDown() {
        for(String token: userAccessTokensForDelete) {
            if(!token.isEmpty()) {
                client.delete(token);
            }
        }
    }

    public String createUserAndGetAccessToken(User user) {
        ValidatableResponse responseCreate = client.create(user);
        assertThat("Создание пользователя, код ответа должен быть 200 OK.", responseCreate.extract().statusCode(), is(SC_OK));
        String accessToken = responseCreate.extract().as(UserRegisterResponse.class).getAccessTokenWithoutBearerWord();
        userAccessTokensForDelete.add(accessToken);
        return accessToken;
    }
}
