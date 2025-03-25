package com.example.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.main.interfaces.ApiService;
import com.example.main.models.BookingReq;
import com.example.main.models.UserInfoResponse;
import com.example.main.retrofits.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
                if(totalCost<=0)
                {
                    Toast.makeText(CheckoutActivity.this, "Invalid Payment", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    selectedPaymentMethod = intent.getStringExtra("selectedPaymentMethod");
                    txtMethodName.setText(selectedPaymentMethod);

                    if ("VnPay".equals(selectedPaymentMethod)) {
                        LoadUserInfo(new UserInfoCallback() {
                            @Override
                            public void onSuccess(UserInfoResponse userInfo) {
                                UserInfoResponse.UserData user = userInfo.getData();
                                SaveBooking(user);
                                CheckOut(totalCost);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(CheckoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        LoadUserInfo(new UserInfoCallback() {
                            @Override
                            public void onSuccess(UserInfoResponse userInfo) {
                                UserInfoResponse.UserData user = userInfo.getData();
                                SaveBooking(user);

                                Intent intent = new Intent(CheckoutActivity.this, LastCheckoutActivity.class);
                                startActivity(intent);
                                finish(); // Đóng activity
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(CheckoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });

        btnChange.setOnClickListener(v -> {
            Intent intentChange = new Intent(CheckoutActivity.this, ChoosePaymentMethodActivity.class);
            selectedPaymentMethod = txtMethodName.getText().toString();
            intentChange.putExtra("selectedPaymentMethod", selectedPaymentMethod);
            intentChange.putExtra("serviceId", serviceId);

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
                        Intent chooseMethodIntent = result.getData();
                        if (chooseMethodIntent != null) {
                            selectedPaymentMethod = chooseMethodIntent.getStringExtra("selectedPayment");
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
    private void CheckOut(int totalCost){
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

    private void SaveBooking(UserInfoResponse.UserData user) {
        String customerId = user.getId(); // ID khách hàng (có thể lấy từ API current-user)
        String serviceId = getIntent().getStringExtra("serviceId");
        String workingDate = getIntent().getStringExtra("selected_day");
        String workingTime = getIntent().getStringExtra("selected_time");
        String address = getIntent().getStringExtra("location");
        String note = getIntent().getStringExtra("note");

        BookingReq request = new BookingReq(customerId, serviceId, workingDate, workingTime, address, note);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        String token = sharedPreferences.getString("ACCESS_TOKEN", "");

        Call<Void> call = apiService.saveBooking("Bearer " + token, request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Dat lich thanh cong!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Lỗi khi đặt lịch!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Kết nối thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadUserInfo(UserInfoCallback callback) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        String token = sharedPreferences.getString("ACCESS_TOKEN", "");

        Call<UserInfoResponse> call = apiService.getUserInfo("Bearer " + token);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không thể lấy thông tin người dùng!");
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                callback.onFailure("Lỗi: " + t.getMessage());
            }
        });
    }

    interface UserInfoCallback {
        void onSuccess(UserInfoResponse userInfo);
        void onFailure(String errorMessage);
    }

}