package com.example.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private Button btnSelectDate, btnSelectTime;
    private TextView tvServiceName, tvCategoryName;
    private EditText etNote;
    private ImageView btnBack, btnShare, btnFavorite;

    private TextView btnContinue;

    private String selectedDay = "";
    private String selectedTime = "";
    private String formattedDate = ""; // yyyy-MM-dd
    private String formattedTime = ""; // HH:mm:ss

    private String servicePrice = "";
    private String serviceId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initViews();

        // Nhận dữ liệu từ Intent
        serviceId = getIntent().getStringExtra("serviceId");
        String categoryName = getIntent().getStringExtra("categoryName");
        String serviceName = getIntent().getStringExtra("serviceName");
        servicePrice = getIntent().getStringExtra("servicePrice");

        // Kiểm tra xem dữ liệu có null không trước khi setText
        if (categoryName != null) {
            tvCategoryName.setText(categoryName);
        }

        if (serviceName != null) {
            tvServiceName.setText(serviceName);
        }

        setupListeners();

    }

    private void initViews() {

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);

        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvServiceName = findViewById(R.id.tvServiceName);

        etNote = findViewById(R.id.etNote);

        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnFavorite = findViewById(R.id.btnFavorite);
    }

    private void setupListeners() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingActivity.this, "Chia sẻ", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện cho nút Continue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDay.isEmpty() || selectedTime.isEmpty()) {
                    Toast.makeText(BookingActivity.this,
                            "Vui lòng chọn ngày và giờ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển sang màn hình Customer Info
                Intent intent = new Intent(BookingActivity.this, CustomerInfoActivity.class);
                intent.putExtra("serviceId", serviceId);

                intent.putExtra("categoryName", tvCategoryName.getText());
                intent.putExtra("serviceName", tvServiceName.getText());
                intent.putExtra("servicePrice", servicePrice);

                intent.putExtra("selected_day", formattedDate);
                intent.putExtra("selected_time", formattedTime);
                intent.putExtra("note", etNote.getText().toString());
                startActivity(intent);
            }
        });

        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Lưu ngày hiển thị trên nút
                        selectedDay = dayOfMonth + " / " + (month1 + 1) + " / " + year1;
                        btnSelectDate.setText(selectedDay);

                        // Chuyển đổi sang định dạng yyyy-MM-dd
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, month1, dayOfMonth);
                        formattedDate = sdf.format(selectedDate.getTime()); // Lưu đúng format
                    }, year, month, day);
            datePickerDialog.show();
        });


        btnSelectTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        // Hiển thị trên button
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                        btnSelectTime.setText(selectedTime);

                        // Chuyển đổi sang định dạng HH:mm:ss
                        formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", selectedHour, selectedMinute, 0);
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }


//    private void updateDaySelection(View selectedView) {
//        dayToday.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        dayMon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        dayTues.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        dayWed.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//
//        selectedView.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.holo_blue_dark)));
//    }
//
//    private void updateTimeSelection(View selectedView) {
//        time7am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        time730am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        time8am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
//        selectedView.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.holo_blue_dark)));
//    }
}