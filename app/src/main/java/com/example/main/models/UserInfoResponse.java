package com.example.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoResponse {
    @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }

    public class UserData {
        @SerializedName("Id")
        private String id;

        @SerializedName("Active")
        private boolean active;

        @SerializedName("Avatar")
        private String avatar;

        @SerializedName("Address")
        private String address;

        @SerializedName("IsVerified")
        private boolean isVerified;

        @SerializedName("Fullname")
        private String fullname;

        @SerializedName("Gender")
        private String gender;

        @SerializedName("Birthday")
        private String birthday;

        @SerializedName("UserName")
        private String username;

        @SerializedName("Email")
        private String email;

        @SerializedName("Balance")
        private int balance;

        @SerializedName("Roles")
        private List<String> roles;

        public String getId() {
            return id;
        }

        public boolean isActive() {
            return active;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getAddress() {
            return address;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public String getFullname() {
            return fullname;
        }

        public String getGender() {
            return gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public int getBalance() {
            return balance;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}
