package org.example.user;

import com.github.javafaker.Faker;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserDataProvider {

    public static User getDefaultUser() {
        return new User("celestial0288@host.domain", "sUw#7peM", "celestial0288");
    }

    public static User getUserWithRandomCredentials() {
        Faker faker = new Faker();
        return new User(
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                faker.name().username()
        );
    }

    public static User getUserWithoutEmail() {
        return new User(null, "sUw#7peM", "spectacle0288");
    }

    public static User getUserWithEmptyEmail() {
        return new User("", "sUw#7peM", "spectacle0288");
    }

    public static User getUserWithoutPassword() {
        return new User("spectacle0288@host.domain", null, "spectacle0288");
    }

    public static User getUserWithEmptyPassword() {
        return new User("spectacle0288@host.domain", "", "spectacle0288");
    }

    public static User getUserWithoutName() {
        return new User("spectacle0288@host.domain", "sUw#7peM", null);
    }

    public static User getUserWithEmptyName() {
        return new User("spectacle0288@host.domain", "sUw#7peM", "");
    }

    public static User getUserWithWrongLoginAndPassword() {
        return new User("spectacle0555@host.domain", "cXnVrTkq", "spectacle0555");
    }
}
