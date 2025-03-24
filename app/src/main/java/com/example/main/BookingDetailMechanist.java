package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class BookingDetailMechanist extends AppCompatActivity {
    private TextView tvServiceName, tvBookingDate, tvWorkingDate, tvWorkingTime, tvAddress, tvNote;
    private Spinner spinnerStatus;
    private Button btnUpdate;

    private String bookingId;
    private String[] statusOptions = {"Pending", "Cancelled", "Done"};

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
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Populate spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(adapter);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null) {
            bookingId = intent.getStringExtra("bookingId");
            tvServiceName.setText(intent.getStringExtra("serviceName"));
            tvBookingDate.setText("Booking Date: " + intent.getStringExtra("bookingDate"));
            tvWorkingDate.setText("Working Date: " + intent.getStringExtra("workingDate"));
            tvWorkingTime.setText("Working Time: " + intent.getStringExtra("workingTime"));
            tvAddress.setText("Address: " + intent.getStringExtra("address"));
            tvNote.setText("Note: " + intent.getStringExtra("note"));

            // Set spinner selection based on status
            String status = intent.getStringExtra("status");
            if (status != null) {
                int position = Arrays.asList(statusOptions).indexOf(status);
                spinnerStatus.setSelection(position >= 0 ? position : 0);
            }
        }

        // Update button click listener
        btnUpdate.setOnClickListener(view -> updateBookingStatus());
    }
    private void updateBookingStatus() {
    }
}


