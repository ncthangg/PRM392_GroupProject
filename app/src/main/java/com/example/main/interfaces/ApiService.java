package com.example.main.interfaces;

import com.example.main.models.AuthResponse;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;

import java.util.List;

import model.GetServiceRes;
import model.ServiceItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("authentications/customers")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @POST("authentications/login")
    Call<AuthResponse> signIn(@Body SignInRequest request);

    @POST("repair-services/get-services")
    Call<GetServiceRes> getServices();
    @POST("repair-services")
    Call<Void> createService(@Body ServiceItem service);

    @PUT("repair-services/{id}")
    Call<Void> updateService(@Path("id") String id, @Body ServiceItem service);

    @DELETE("repair-services/{id}")
    Call<Void> deleteService(@Path("id") String id);

}

