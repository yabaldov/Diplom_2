package org.example.user;

public class UserPersonalData {

    private String email;
    private String name;

    public UserPersonalData() {
    }

    public UserPersonalData(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserPersonalData(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
