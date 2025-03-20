package com.example.main;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentMethodActivity extends AppCompatActivity {
    private TextView tvTestValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_method);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTestValue = findViewById(R.id.tvTestValue);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String gender = getIntent().getStringExtra("gender");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String country = getIntent().getStringExtra("country");
        String selectedDay = getIntent().getStringExtra("selected_day");
        String selectedTime = getIntent().getStringExtra("selected_time");
        String note = getIntent().getStringExtra("note");

        String message = "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Gender: " + gender + "\n"
                + "Phone: " + phoneNumber + "\n"
                + "Country: " + country + "\n"
                + "Selected Day: " + selectedDay + "\n"
                + "Selected Time: " + selectedTime + "\n"
                + "Note: " + note;

        tvTestValue.setText(message);
    }
}