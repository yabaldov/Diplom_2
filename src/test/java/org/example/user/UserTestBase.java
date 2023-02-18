package org.example.user;

import io.qameta.allure.Step;
import org.example.base.TestBase;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public abstract class UserTestBase extends TestBase {

    protected List<String> userAccessTokensForDelete;

    @Before
    public void setUp() {
        super.setUp();
        userAccessTokensForDelete = new ArrayList<>();
    }

    @After
    public void tearDown() {
        for(String token: userAccessTokensForDelete) {
            deleteUser(token);
        }
    }

    @Step("Получение и сохранение в списке токена нового пользователя")
    public String getAndSaveAccessTokenOfNewUser(User user) {
        String accessToken = createUserAndReturnAccessToken(user);
        userAccessTokensForDelete.add(accessToken);
        return accessToken;
    }
}
