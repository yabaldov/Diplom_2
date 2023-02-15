package org.example.order;

import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserDataProvider;
import org.example.user.response.UserRegisterResponse;
import org.junit.After;
import org.junit.Before;

public class OrderTestBase {

    protected OrderClient client;
    protected String userAccessToken;
    protected static final String EMPTY_ACCESS_TOKEN = "";

    @Before
    public void setUp() {
        client = new OrderClient();
        userAccessToken = createUserAndReturnAccessToken(UserDataProvider.getDefaultUser());
    }

    @After
    public void tearDown() {
        deleteUser(userAccessToken);
    }

    protected String createUserAndReturnAccessToken(User user) {
        UserClient client = new UserClient();
        ValidatableResponse responseCreate = client.create(user);
        return responseCreate
                .extract()
                .as(UserRegisterResponse.class)
                .getAccessTokenWithoutBearerWord();
    }

    protected void deleteUser(String accessToken) {
        UserClient client = new UserClient();
        if(!accessToken.isEmpty()) {
            client.delete(accessToken);
        }
    }
}
