package com.example.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerInfoActivity extends AppCompatActivity {

    private EditText etName, etEmail, etGender, etPhoneNumber;
    private Spinner spinnerCountry;
    private Button btnContinue, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etGender = findViewById(R.id.etGender);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        setupCountrySpinner();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerInfoActivity.this, "Form submitted successfully", Toast.LENGTH_SHORT).show();
                // Here you would typically navigate to next screen
                // Intent intent = new Intent(CustomerInfoActivity.this, NextActivity.class);
                // startActivity(intent);
            }
        });
    }

    private void setupCountrySpinner() {
        // Sample countries
        String[] countries = new String[]{
                "Viet Nam", "United States", "United Kingdom", "Australia",
                "Canada", "China", "Japan", "South Korea"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        // Set default to Viet Nam (first in list)
        spinnerCountry.setSelection(0);
    }
}