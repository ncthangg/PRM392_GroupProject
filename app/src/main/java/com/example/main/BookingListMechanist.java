package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.interfaces.ApiService;
import com.example.main.retrofits.RetrofitClient;
import com.google.gson.Gson;

import adapter.BookingAdapter;
import model.BookingItem;
import model.GetBookingsRes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListMechanist extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingItem> bookingList = new ArrayList<>();
    private static final int REQUEST_UPDATE_BOOKING = 1; // Define a request code


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_list_mechanist);

        recyclerView = findViewById(R.id.rvBookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bookingAdapter = new BookingAdapter(this, bookingList, bookingItem -> {
            Intent intent = new Intent(BookingListMechanist.this, BookingDetailMechanist.class);

            // Convert numerical values to string
            String statusString = String.valueOf(bookingItem.getStatus());

            // Pass booking details through Intent
            intent.putExtra("bookingId", bookingItem.getId());
            intent.putExtra("serviceName", bookingItem.getService().getName());
            intent.putExtra("bookingDate", bookingItem.getBookingDate() != null ? bookingItem.getBookingDate().toString() : "N/A");
            intent.putExtra("workingDate", bookingItem.getWorkingDate() != null ? bookingItem.getWorkingDate().toString() : "N/A");
            intent.putExtra("workingTime", bookingItem.getWorkingTime() != null ? bookingItem.getWorkingTime().toString() : "N/A");
            intent.putExtra("address", bookingItem.getAddress() != null ? bookingItem.getAddress() : "N/A");
            intent.putExtra("note", bookingItem.getNote() != null ? bookingItem.getNote() : "N/A");
            intent.putExtra("status", statusString);
            startActivityForResult(intent, REQUEST_UPDATE_BOOKING);

        });
        recyclerView.setAdapter(bookingAdapter);
        loadBookingAdmin();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE_BOOKING && resultCode == RESULT_OK) {
            loadBookingAdmin(); // Call API again to refresh the list
        }
    }
    private void loadBookingAdmin() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<GetBookingsRes> call = apiService.getBookings("18493305-2010-4196-83ab-d8e28d73899e", "Pending",1, 10);

        call.enqueue(new Callback<GetBookingsRes>() {
            @Override
            public void onResponse(Call<GetBookingsRes> call, Response<GetBookingsRes> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Data received: " + new Gson().toJson(response.body()));

                    bookingList.clear();
                    if (response.body().data != null && response.body().data.Data != null) {
                        bookingList.addAll(response.body().data.Data);
                        bookingAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API_RESPONSE", "Failed with response: " + response.errorBody());
                    Toast.makeText(BookingListMechanist.this, "Failed to get data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetBookingsRes> call, Throwable t) {
                Log.e("API_ERROR", "Request failed", t);
                Toast.makeText(BookingListMechanist.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
