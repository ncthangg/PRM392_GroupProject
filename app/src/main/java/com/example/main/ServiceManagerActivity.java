package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.interfaces.ApiService;
import com.example.main.models.GetServiceRes;
import com.example.main.models.ServiceItem;
import com.example.main.retrofits.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import adapter.ServiceManagerAdapter;
import com.example.main.models.GetServiceRes;
import com.example.main.models.ServiceItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceManagerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceManagerAdapter serviceAdapter;
    private GoogleSignInClient mGoogleSignInClient;

    private List<ServiceItem> serviceList = new ArrayList<>();
    private FloatingActionButton fabAddItem;
    private static final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_service_manager);

        recyclerView = findViewById(R.id.rvManageItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceAdapter = new ServiceManagerAdapter(this, serviceList, this::onItemClick);
        recyclerView.setAdapter(serviceAdapter);

        fabAddItem = findViewById(R.id.fabAddItem);
        fabAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(ServiceManagerActivity.this, AddServiceActivity.class);
            startActivity(intent);
        });

        loadServices();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        findViewById(R.id.nav_logout).setOnClickListener(v -> logoutUser());
        findViewById(R.id.nav_profile).setOnClickListener(v -> viewProfile());
    }

    private void loadServices() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.getServices().enqueue(new Callback<GetServiceRes>() {
            @Override
            public void onResponse(Call<GetServiceRes> call, Response<GetServiceRes> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    serviceList.clear();
                    if (response.body().data.Data != null && !response.body().data.Data.isEmpty()) {
                        serviceList.addAll(response.body().data.Data);
                    } else {
                        Toast.makeText(ServiceManagerActivity.this, "No services available!", Toast.LENGTH_SHORT).show();
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ServiceManagerActivity.this, "Failed to load services!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetServiceRes> call, Throwable t) {
                Toast.makeText(ServiceManagerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onItemClick(ServiceItem service) {
        Intent intent = new Intent(ServiceManagerActivity.this, EditServiceActivity.class);
        intent.putExtra("service_id", service.getId());
        intent.putExtra("service_name", service.getName());
        intent.putExtra("service_price", service.getPrice());
        intent.putExtra("service_image", service.getImage());
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            loadServices();
        }
    }
    private void logoutUser() {
        // Xóa token trong SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ACCESS_TOKEN"); // Xóa token
        editor.apply(); // Lưu lại thay đổi
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(ServiceManagerActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ServiceManagerActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void viewProfile() {
        Intent intent = new Intent(ServiceManagerActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

}