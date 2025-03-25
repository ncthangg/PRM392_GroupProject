package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.interfaces.ApiService;
import com.example.main.retrofits.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import adapter.ServiceAdapter;
import com.example.main.models.GetServiceRes;
import com.example.main.models.ServiceItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceForCusActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GoogleSignInClient mGoogleSignInClient;
    private ServiceAdapter serviceAdapter;
    private List<ServiceItem> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_service);

        recyclerView = findViewById(R.id.rvServiceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceAdapter = new ServiceAdapter(this, serviceList, serviceItem -> {
            Intent intent = new Intent(ServiceForCusActivity.this, BookingActivity.class);
            int servicePriceInt = serviceItem.getPrice();
            String servicePriceString = Integer.toString(servicePriceInt);
            intent.putExtra("categoryName", serviceItem.getCategory().getName());
            intent.putExtra("serviceId", serviceItem.getId());
            intent.putExtra("serviceName", serviceItem.getName());
            intent.putExtra("servicePrice", servicePriceString);
            startActivity(intent);
        });
        recyclerView.setAdapter(serviceAdapter);
        loadServices();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Bắt sự kiện click
//        findViewById(R.id.nav_home).setOnClickListener(v -> {
//            Intent intent = new Intent(ServiceForCusActivity.this, ServiceForCusActivity.class);
//            startActivity(intent);
//        });

        findViewById(R.id.nav_logout).setOnClickListener(v -> logoutUser());
        findViewById(R.id.nav_profile).setOnClickListener(v -> viewProfile());

    }

    private void loadServices() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<GetServiceRes> call = apiService.getServices();

        Log.d("API_CALL", "Calling getServices API...");
        call.enqueue(new Callback<GetServiceRes>() {
            @Override
            public void onResponse(Call<GetServiceRes> call, Response<GetServiceRes> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    serviceList.clear();
                    if (response.body().data.Data != null && !response.body().data.Data.isEmpty()) {
                        serviceList.addAll(response.body().data.Data);
                    } else {
                        Toast.makeText(ServiceForCusActivity.this, "No services available!", Toast.LENGTH_SHORT).show();
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ServiceForCusActivity.this, "Failed to load services!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetServiceRes> call, Throwable t) {
                Toast.makeText(ServiceForCusActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logoutUser() {
        // Xóa token trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ACCESS_TOKEN"); // Xóa token
        editor.apply(); // Lưu lại thay đổi
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(ServiceForCusActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ServiceForCusActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }    private void viewProfile() {
        Intent intent = new Intent(ServiceForCusActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
