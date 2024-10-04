package com.events.helpers;

import com.events.entities.User;

public class LoginResponse {
    private String token;
    private User user;

    public LoginResponse(String token, User user2) {
        this.token = token;
        this.user = user2;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

}
