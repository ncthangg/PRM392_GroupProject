package com.example.main.models;

import java.util.Date;

public class SignUpRequest {
    private String FullName;
    private String Gender;
    private String Birthday;
    private String Address;
    private String UserName;
    private String Password;
    private String Email;

    public SignUpRequest(String fullName, String gender, String birthday, String address, String userName, String password, String email) {
        FullName = fullName;
        Gender = gender;
        Birthday = birthday;
        Address = address;
        UserName = userName;
        Password = password;
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

