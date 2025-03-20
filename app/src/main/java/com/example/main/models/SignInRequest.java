package com.example.main.models;

public class SignInRequest {
    private String userName;
    private String password;

    public SignInRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
