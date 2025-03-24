package com.example.main.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("data") // Ánh xạ với key "data"
    private TokenData data;

    public TokenData getData() {
        return data;
    }

    public class TokenData {
        @SerializedName("AccessToken")
        private String accessToken;

        @SerializedName("RefreshToken")
        private String refreshToken;

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}

