package org.example.user;

public class UserCredentials {

    private String email;
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
