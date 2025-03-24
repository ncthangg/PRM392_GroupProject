package com.example.main.interfaces;

import com.example.main.models.AuthResponse;
import com.example.main.models.BookingReq;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;
import com.example.main.models.UserInfoResponse;

import com.example.main.models.GetServiceRes;

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

    @POST("bookings")
    Call<Void> saveBooking(@Header("Authorization") String token, @Body BookingReq request);

}

