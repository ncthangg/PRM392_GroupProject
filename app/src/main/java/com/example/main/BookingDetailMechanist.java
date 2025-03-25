package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.interfaces.ApiService;
import com.example.main.models.BookingUpdateRequest;
import com.example.main.retrofits.RetrofitClient;

import java.util.Arrays;

import com.example.main.models.BookingUpdateRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailMechanist extends AppCompatActivity {
    private TextView tvServiceName, tvBookingDate, tvWorkingDate, tvWorkingTime, tvAddress, tvNote;
    private Spinner spinnerStatus;
    private Button btnUpdate;

    private String bookingId;
    private String[] statusOptions = {"Pending", "Cancelled", "Accepted"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail_mechanist);

        tvServiceName = findViewById(R.id.tvServiceName);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvWorkingDate = findViewById(R.id.tvWorkingDate);
        tvWorkingTime = findViewById(R.id.tvWorkingTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvNote = findViewById(R.id.tvNote);
        btnUpdate = findViewById(R.id.btnUpdate);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        // Populate spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(adapter);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            bookingId = intent.getStringExtra("bookingId");
            tvServiceName.setText(intent.getStringExtra("serviceName"));
            tvBookingDate.setText(intent.getStringExtra("bookingDate"));
            tvWorkingDate.setText(intent.getStringExtra("workingDate"));
            tvWorkingTime.setText(intent.getStringExtra("workingTime"));
            tvAddress.setText(intent.getStringExtra("address"));
            tvNote.setText(intent.getStringExtra("note"));

            // Set spinner selection based on status
            String status = intent.getStringExtra("status");
            if (status != null) {
                int position = Arrays.asList(statusOptions).indexOf(status);
                spinnerStatus.setSelection(position >= 0 ? position : 0);
            }

            boolean isEditable = getIntent().getBooleanExtra("isEditable", true);
            Button btnUpdate = findViewById(R.id.btnUpdate);
            if (!isEditable) {
                btnUpdate.setVisibility(View.GONE);  // Ẩn nút cập nhật
                spinnerStatus.setEnabled(false);
            }

        }

        // Update button click listener
        btnUpdate.setOnClickListener(view -> updateBookingStatus(intent.getStringExtra("bookingId")));

    }
    private void updateBookingStatus(String id) {
        String selectedStatus = spinnerStatus.getSelectedItem().toString();

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        // Send status as JSON body
        BookingUpdateRequest request = new BookingUpdateRequest(selectedStatus);
        Call<Void> call = apiService.updateBookingStatus(id, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.code() == 204) {  // ✅ Handle No Content correctly
                    Toast.makeText(BookingDetailMechanist.this, "Status Updated Successfully", Toast.LENGTH_SHORT).show();

                    // Send result back to BookingListActivity
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish(); // Close BookingDetailMechanist
                } else {
                    Toast.makeText(BookingDetailMechanist.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Request failed", t);
                Toast.makeText(BookingDetailMechanist.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}


