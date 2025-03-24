package com.example.main.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.ServiceForCusActivity;
import com.example.main.interfaces.ApiService;
import com.example.main.retrofits.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import adapter.ServiceAdapter;
import model.GetServiceRes;
import model.ServiceItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FragmentServiceList extends Fragment {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<ServiceItem> serviceList = new ArrayList<>();

    public FragmentServiceList() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        adapter = new ServiceAdapter(getContext(), serviceList, service -> {
//            Toast.makeText(getContext(), "Chọn: " + service.getName(), Toast.LENGTH_SHORT).show();
//        });

        recyclerView.setAdapter(adapter);

        fetchServices(); // Gọi API để lấy danh sách dịch vụ
        return view;
    }

    private void fetchServices() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetServiceRes> call = apiService.getServices();

        call.enqueue(new Callback<GetServiceRes>() {
            @Override
            public void onResponse(Call<GetServiceRes> call, Response<GetServiceRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList.clear();
                    if (response.body().data != null && response.body().data.Data != null) {
                        serviceList.addAll(response.body().data.Data);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to get data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetServiceRes> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load services", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
