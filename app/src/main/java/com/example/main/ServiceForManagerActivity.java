package com.example.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.interfaces.ApiService;
import com.example.main.retrofits.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import adapter.ServiceAdapter;
import adapter.ServiceManagerAdapter;
import model.GetServiceRes;
import model.ServiceItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceForManagerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceManagerAdapter serviceManagerAdapter;
    private List<ServiceItem> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_service_manager);

        recyclerView = findViewById(R.id.rvManageItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceManagerAdapter = new ServiceManagerAdapter(this, serviceList);
        recyclerView.setAdapter(serviceManagerAdapter);

        loadServices();
    }

    private void loadServices() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<GetServiceRes> call = apiService.getServices();

        call.enqueue(new Callback<GetServiceRes>() {
            @Override
            public void onResponse(Call<GetServiceRes> call, Response<GetServiceRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList.clear();
                    if (response.body().data != null && response.body().data.Data != null) {
                        serviceList.addAll(response.body().data.Data);
                        serviceManagerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ServiceForManagerActivity.this, "Failed to get data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetServiceRes> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load services", t);
                Toast.makeText(ServiceForManagerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
