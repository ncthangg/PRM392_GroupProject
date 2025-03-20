package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerInfoActivity extends AppCompatActivity {

    private EditText etName, etEmail, etGender, etPhoneNumber;
    private Spinner spinnerCountry;
    private TextView btnContinue;
    private ImageView btnBack;

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

        String selectedDay = getIntent().getStringExtra("selected_day");
        String selectedTime = getIntent().getStringExtra("selected_time");
        String note = getIntent().getStringExtra("note");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String gender = etGender.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String country = spinnerCountry.getSelectedItem().toString();

                if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || phoneNumber.isEmpty() || country.isEmpty()) {
                    Toast.makeText(CustomerInfoActivity.this, "Input invalidate", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CustomerInfoActivity.this, PaymentMethodActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("gender", gender);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("country", country);
                    intent.putExtra("selected_day", selectedDay);
                    intent.putExtra("selected_time", selectedTime);
                    intent.putExtra("note", note);
                    startActivity(intent);
                }

            }
        });
    }

    private void setupCountrySpinner() {
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