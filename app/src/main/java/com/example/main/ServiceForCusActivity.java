package com.example.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapter.ServiceAdapter;
import api.ServiceApis;
import api.ApiService;
import model.GetServiceRes;
import model.ServiceItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceForCusActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private List<ServiceItem> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_service);

        recyclerView = findViewById(R.id.rvServiceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceAdapter = new ServiceAdapter(this, serviceList);
        recyclerView.setAdapter(serviceAdapter);

        loadServices();
    }

    private void loadServices() {
        ServiceApis apiService = ApiService.getClient().create(ServiceApis.class);
        Call<GetServiceRes> call = apiService.getServices();

        call.enqueue(new Callback<GetServiceRes>() {
            @Override
            public void onResponse(Call<GetServiceRes> call, Response<GetServiceRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList.clear();
                    if (response.body().data != null && response.body().data.Data != null) {
                        serviceList.addAll(response.body().data.Data);
                        serviceAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ServiceForCusActivity.this, "Failed to get data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetServiceRes> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load services", t);
                Toast.makeText(ServiceForCusActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
