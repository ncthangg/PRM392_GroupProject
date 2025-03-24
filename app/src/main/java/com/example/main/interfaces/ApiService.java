package com.example.main.interfaces;

import com.example.main.models.AuthResponse;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;
import com.example.main.models.UserInfoResponse;

import java.util.List;

import model.GetServiceRes;
import model.ServiceItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("authentications/customers")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @POST("authentications/login")
    Call<AuthResponse> signIn(@Body SignInRequest request);

    @POST("repair-services/get-services")
    Call<GetServiceRes> getServices();

    @GET("authentications/current-user")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

}

