package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.interfaces.ApiService;
import com.example.main.models.UserInfoResponse;
import com.example.main.retrofits.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerInfoActivity extends AppCompatActivity {

    private EditText etName, etEmail, etLocation, etPhoneNumber;
    private TextView btnContinue;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etLocation = findViewById(R.id.etLocation);

        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);

        String serviceId = getIntent().getStringExtra("serviceId");

        String categoryName = getIntent().getStringExtra("categoryName");
        String serviceName = getIntent().getStringExtra("serviceName");
        String servicePrice = getIntent().getStringExtra("servicePrice");

        String selectedDay = getIntent().getStringExtra("selected_day");
        String selectedTime = getIntent().getStringExtra("selected_time");
        String note = getIntent().getStringExtra("note");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });

        loadUserInfo();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String location = etLocation.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
                    Toast.makeText(CustomerInfoActivity.this, "Input invalidate", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CustomerInfoActivity.this, ChoosePaymentMethodActivity.class);
                    intent.putExtra("serviceId", serviceId);

                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("serviceName", serviceName);
                    intent.putExtra("servicePrice", servicePrice);

                    intent.putExtra("name", name);
                    intent.putExtra("email", email);

                    intent.putExtra("phone", phoneNumber);
                    intent.putExtra("location", location);

                    intent.putExtra("selected_day", selectedDay);
                    intent.putExtra("selected_time", selectedTime);
                    intent.putExtra("note", note);
                    startActivity(intent);
                }

            }
        });
    }

    private void loadUserInfo() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        String token = sharedPreferences.getString("ACCESS_TOKEN", "");

        Call<UserInfoResponse> call = apiService.getUserInfo("Bearer " + token); // Gọi API lấy thông tin người dùng

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserInfoResponse.UserData user = response.body().getData();

                    // Cập nhật dữ liệu vào các EditText
                    etName.setText(user.getFullname());
                    etEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(CustomerInfoActivity.this, "Không thể lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(CustomerInfoActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}