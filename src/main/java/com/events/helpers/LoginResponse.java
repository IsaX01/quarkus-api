package com.events.helpers;

public class LoginResponse {
    private String token;
    private String username;
    private String roleName;

    public LoginResponse(String token, String username, String roleName) {
        this.token = token;
        this.username = username;
        this.roleName = roleName;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRoleName() {
        return roleName;
    }
}
