package org.example.user.response;

public class UserDataResponse {

    private Boolean success;
    private UserRegistered user;

    public UserDataResponse() {
    }

    public UserDataResponse(Boolean success, UserRegistered user) {
        this.success = success;
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserRegistered getUser() {
        return user;
    }

    public void setUser(UserRegistered user) {
        this.user = user;
    }
}
