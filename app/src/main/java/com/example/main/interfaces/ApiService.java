package com.example.main.interfaces;

import com.example.main.models.AuthResponse;
import com.example.main.models.BookingReq;
import com.example.main.models.BookingUpdateRequest;
import com.example.main.models.Category;
import com.example.main.models.GetBookingsRes;
import com.example.main.models.ServiceItem;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;
import com.example.main.models.UserInfoResponse;

import com.example.main.models.BookingUpdateRequest;
import com.example.main.models.GetBookingsRes;
import com.example.main.models.GetServiceRes;
import com.example.main.models.ServiceItem;
import com.example.main.models.ServiceReq;
import com.example.main.models.GetServiceRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    @POST("authentications/customers")
    Call<Void> signUp(@Body SignUpRequest request);

    @POST("authentications/login")
    Call<AuthResponse> signIn(@Body SignInRequest request);
//    @GET("api/categories")
//    Call<List<Category>> getCategories();

    @POST("repair-services/get-services")
    Call<GetServiceRes> getServices();
    @POST("repair-services")
    Call<Void> createService(@Body ServiceReq serviceReq);

    @PUT("repair-services/{id}")
    Call<Void> updateService(@Path("id") String id, @Body ServiceReq service);

    @DELETE("repair-services/{id}")
    Call<Void> deleteService(@Path("id") String id);

    @GET("authentications/current-user")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

    @POST("bookings")
    Call<Void> saveBooking(@Header("Authorization") String token, @Body BookingReq request);

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

