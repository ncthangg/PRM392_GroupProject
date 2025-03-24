package com.example.main.interfaces;

import com.example.main.models.AuthResponse;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;
import com.example.main.models.UserInfoResponse;

import model.BookingUpdateRequest;
import model.GetBookingsRes;
import model.GetServiceRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("authentications/customers")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @POST("authentications/login")
    Call<AuthResponse> signIn(@Body SignInRequest request);

    @POST("repair-services/get-services")
    Call<GetServiceRes> getServices();

    @GET("authentications/current-user")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);
        @POST("bookings/get-bookings-by-mechanist/{mechanistId}")
        Call<GetBookingsRes> getBookings(
                @Path("mechanistId") String mechanistId,
                @Query("Status") String status,
                @Query("PageNumber") int pageNumber,
                @Query("PageSize") int pageSize
        );
    @PUT("bookings/{id}")
    Call<Void> updateBookingStatus(
            @Path("id") String id,
            @Body BookingUpdateRequest request
    );
}

