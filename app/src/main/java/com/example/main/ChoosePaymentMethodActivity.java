package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosePaymentMethodActivity extends AppCompatActivity {

    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_payment_method);

        RadioGroup radioGroup = findViewById(R.id.radioGroupPayment);
        RadioButton rbCash = findViewById(R.id.rbCash);
        RadioButton rbVnPay = findViewById(R.id.rbVnPay);

        LinearLayout cashLayout = findViewById(R.id.cashLayout);
        LinearLayout vnPayLayout = findViewById(R.id.vnpayLayout);

        cashLayout.setOnClickListener(v -> radioGroup.check(R.id.rbCash));
        vnPayLayout.setOnClickListener(v -> radioGroup.check(R.id.rbVnPay));

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Nhận dữ liệu từ CusomterInfoActivity
        String serviceId = getIntent().getStringExtra("serviceId");

        String categoryName = getIntent().getStringExtra("categoryName");
        String serviceName = getIntent().getStringExtra("serviceName");
        String servicePrice = getIntent().getStringExtra("servicePrice");

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String location = getIntent().getStringExtra("location");

        String selectedDay = getIntent().getStringExtra("selected_day");
        String selectedTime = getIntent().getStringExtra("selected_time");
        String note = getIntent().getStringExtra("note");

        // Nhận dữ liệu từ CheckoutActivity
        String selectedPayment = getIntent().getStringExtra("selectedPaymentMethod");
        if(selectedPayment != null){
            // Chọn lại radio button
            if ("Cash".equals(selectedPayment)) {
                cashLayout.setOnClickListener(v -> radioGroup.check(R.id.rbCash));
            } else if ("VnPay".equals(selectedPayment)) {
                vnPayLayout.setOnClickListener(v -> radioGroup.check(R.id.rbVnPay));
            }
        }

        btnConfirmPayment.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String paymentMethod = "";

            if (selectedId == R.id.rbCash) {
                paymentMethod = "Cash";
            } else if (selectedId == R.id.rbVnPay) {
                paymentMethod = "VnPay";
            }

            Intent intent  = new Intent(ChoosePaymentMethodActivity.this, CheckoutActivity.class);
            intent.putExtra("selectedPaymentMethod", paymentMethod);
            intent.putExtra("serviceId", serviceId);

            intent.putExtra("categoryName", categoryName);
            intent.putExtra("serviceName", serviceName);
            intent.putExtra("servicePrice", servicePrice);

            intent.putExtra("name", name);
            intent.putExtra("email", email);

            intent.putExtra("phone", phone);
            intent.putExtra("location", location);

            intent.putExtra("selected_day", selectedDay);
            intent.putExtra("selected_time", selectedTime);
            intent.putExtra("note", note);

            startActivity(intent);
            finish();
        });



    }

}
