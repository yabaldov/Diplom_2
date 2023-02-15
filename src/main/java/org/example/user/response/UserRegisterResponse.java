package org.example.user.response;

public class UserRegisterResponse {

    private Boolean success;
    private UserRegistered user;
    private String accessToken;
    private String refreshToken;

    public String getAccessTokenWithoutBearerWord() {
        return getAccessToken().split(" ")[1];
    }

    public UserRegisterResponse() {
    }

    public UserRegisterResponse(Boolean success, UserRegistered user, String accessToken, String refreshToken) {
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
