package com.example.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;

import vnpayConfig.VNPay;

public class CheckoutActivity extends AppCompatActivity {

    private TextView txtServiceType;
    private TextView txtServiceName;
    private TextView txtServicePrice;

    private TextView txtCustomerName;
    private TextView txtEmail;
    private TextView txtPhoneNumber;
    private TextView txtLocation;

    private TextView txtBookingDate;
    private TextView txtNote;

    private TextView txtTotalPrice;

    private ImageView imageViewMethod;
    private TextView txtMethodName;

    private Button btnCheckout;
    private ConstraintLayout btnChange;

    private int totalCost = 0;

    private String selectedPaymentMethod = "";
    private ActivityResultLauncher<Intent> choosePaymentMethodLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        txtServiceType = findViewById(R.id.txtServiceType);
        txtServiceName = findViewById(R.id.txtServiceName);
        txtServicePrice = findViewById(R.id.txtServicePrice);

        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtLocation = findViewById(R.id.txtLocation);
        txtBookingDate = findViewById(R.id.txtBookingDate);
        txtTotalPrice =  findViewById(R.id.txtTotalPrice);

        imageViewMethod = findViewById(R.id.imageViewMethod);
        txtMethodName = findViewById(R.id.txtMethodName);

        btnCheckout = findViewById(R.id.btnContinue);
        btnChange = findViewById(R.id.btnChange);

        // Nhận dữ liệu từ ChoosePaymentMethodActivity
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

        //Show dữ liệu ra
        txtServiceType.setText(categoryName);
        txtServiceName.setText(serviceName);

        txtCustomerName.setText(name);
        txtEmail.setText(email);
        txtPhoneNumber.setText(phone);
        txtLocation.setText(location);

        txtBookingDate.setText(selectedDay + " - " + selectedTime);

        //Show TotalPrice
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(Integer.parseInt(servicePrice));
        txtServicePrice.setText(formattedPrice);
        txtTotalPrice.setText(formattedPrice);

        String totalCostStr = txtTotalPrice.getText().toString().trim();
        totalCostStr = totalCostStr.replace(".", "").replace(",", ".");
        totalCost = Integer.parseInt(totalCostStr);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedPaymentMethod")) {
            selectedPaymentMethod = intent.getStringExtra("selectedPaymentMethod");
            txtMethodName.setText(selectedPaymentMethod);

            // Đổi icon theo phương thức thanh toán
            if ("VnPay".equals(selectedPaymentMethod)) {
                imageViewMethod.setImageResource(R.drawable.ic_vnpay);
            } else {
                imageViewMethod.setImageResource(R.drawable.ic_cash);
            }
        }

        //Khai báo button
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalCost<=0){
                    Toast.makeText(CheckoutActivity.this, "Invalid Payment", Toast.LENGTH_SHORT).show();
                }else
                    CheckOut(totalCost);
            }
        });

        btnChange.setOnClickListener(v -> {
            Intent intentChange = new Intent(CheckoutActivity.this, ChoosePaymentMethodActivity.class);
            selectedPaymentMethod = txtMethodName.getText().toString();
            intentChange.putExtra("selectedPaymentMethod", selectedPaymentMethod);
            intentChange.putExtra("categoryName", categoryName);
            intentChange.putExtra("serviceName", serviceName);
            intentChange.putExtra("servicePrice", servicePrice);

            intentChange.putExtra("name", name);
            intentChange.putExtra("email", email);

            intentChange.putExtra("phone", phone);
            intentChange.putExtra("location", location);

            intentChange.putExtra("selected_day", selectedDay);
            intentChange.putExtra("selected_time", selectedTime);
            intentChange.putExtra("note", note);

            choosePaymentMethodLauncher.launch(intentChange);
        });

        choosePaymentMethodLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedPaymentMethod = data.getStringExtra("selectedPayment");
                            txtMethodName.setText(selectedPaymentMethod);
                            imageViewMethod.setImageResource(
                                    "VnPay".equals(selectedPaymentMethod) ?
                                            R.drawable.ic_vnpay : R.drawable.ic_cash
                            );
                        }
                    }
                }
        );

    }


    protected void CheckOut(int totalCost){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Checkout?")
                .setMessage("Payment for Service: " + totalCost)
                .setPositiveButton("Yes", (dialog, which) -> {

                    Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();

                    //QUAN TRỌNG
                    String orderId = String.valueOf(System.currentTimeMillis());
                    String paymentUrl = null;

                    try {
                        paymentUrl = VNPay.getPaymentUrl(orderId, totalCost);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
                    startActivity(intent);
                    //QUAN TRỌNG

                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()) // Đóng dialog nếu hủy
                .show();
    }
}