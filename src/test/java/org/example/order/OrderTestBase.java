package org.example.order;

import org.example.base.TestBase;
import org.example.user.UserDataProvider;
import org.junit.After;
import org.junit.Before;

public class OrderTestBase extends TestBase {

    protected OrderClient orderClient;
    protected String randomUserAccessToken;

    @Before
    public void setUp() {
        super.setUp();
        orderClient = new OrderClient();
        randomUserAccessToken = createUserAndReturnAccessToken(
                UserDataProvider.getUserWithRandomCredentials()
        );
    }

    @After
    public void tearDown() {
        deleteUser(randomUserAccessToken);
    }
}

