package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailMechanist extends AppCompatActivity {

    private TextView tvServiceName, tvBookingDate, tvWorkingDate, tvWorkingTime, tvAddress, tvNote, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail_mechanist);

        // Initialize views
        tvServiceName = findViewById(R.id.tvServiceName);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvWorkingDate = findViewById(R.id.tvWorkingDate);
        tvWorkingTime = findViewById(R.id.tvWorkingTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvNote = findViewById(R.id.tvNote);
        tvStatus = findViewById(R.id.tvStatus);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            tvServiceName.setText(intent.getStringExtra("serviceName"));
            tvBookingDate.setText(intent.getStringExtra("bookingDate"));
            tvWorkingDate.setText(intent.getStringExtra("workingDate"));
            tvWorkingTime.setText(intent.getStringExtra("workingTime"));
            tvAddress.setText(intent.getStringExtra("address"));
            tvNote.setText(intent.getStringExtra("note"));
            tvStatus.setText(intent.getStringExtra("status"));
        }
    }
}

