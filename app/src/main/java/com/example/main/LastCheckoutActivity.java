package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.main.interfaces.ApiService;
import com.example.main.models.BookingReq;
import com.example.main.models.UserInfoResponse;
import com.example.main.retrofits.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastCheckoutActivity extends AppCompatActivity {
    private Button btnMyBooking,btnContinue;
    private ImageView icResult;
    private TextView txtTitle;
    private TextView txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_last_checkout);

        btnMyBooking = findViewById(R.id.btnMyBooking);
        btnContinue = findViewById(R.id.btnContinue);

        icResult = findViewById(R.id.imgResult);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(LastCheckoutActivity.this, ServiceForCusActivity.class);
                 startActivity(intent);
                 finish(); // Đóng activity
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null) {
            String responseCode = uri.getQueryParameter("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                icResult.setImageResource(R.drawable.ic_success); // Đổi icon thành công
                txtTitle.setText("Thanh toán thành công!");
                txtDescription.setText("Cảm ơn bạn đã sử dụng dịch vụ.");
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
            } else {
                icResult.setImageResource(R.drawable.ic_fail); // Đổi icon thất bại
                txtTitle.setText("Thanh toán thất bại!");
                txtDescription.setText("Giao dịch không thành công, vui lòng thử lại.");
                Toast.makeText(this, "Thanh toán thất bại hoặc bị hủy!", Toast.LENGTH_LONG).show();
            }
        }
    }


}