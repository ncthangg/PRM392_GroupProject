package com.example.main.models;

public class SignUpRequest {
    private String fullName;
    private String address;
    private String userName;
    private String password;
    private String email;


    public SignUpRequest(String fullName, String address, String userName, String password, String email) {
        this.fullName = fullName;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
}

