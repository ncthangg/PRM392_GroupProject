package com.example.main;

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

public class BookingActivity extends AppCompatActivity {

    private LinearLayout dayToday, dayMon, dayTues, dayWed;
    private LinearLayout time7am, time730am, time8am;
    private TextView tvServiceName, tvCategoryName;
    private EditText etNote;
    private ImageView btnBack, btnShare, btnFavorite;

    private TextView btnContinue;

    private String selectedDay = "";
    private String selectedTime = "";
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
        // Ngày
        dayToday = findViewById(R.id.dayToday);
        dayMon = findViewById(R.id.dayMon);
        dayTues = findViewById(R.id.dayTues);
        dayWed = findViewById(R.id.dayWed);


        time7am = findViewById(R.id.time7am);
        time730am = findViewById(R.id.time730am);
        time8am = findViewById(R.id.time8am);

        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvServiceName = findViewById(R.id.tvServiceName);

        etNote = findViewById(R.id.etNote);

        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnFavorite = findViewById(R.id.btnFavorite);
    }

    private void setupListeners() {
        dayToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDaySelection(dayToday);
                selectedDay = "Today (4 Oct)";
            }
        });

        dayMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDaySelection(dayMon);
                selectedDay = "Monday (6 Oct)";
            }
        });

        dayTues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDaySelection(dayTues);
                selectedDay = "Tuesday (7 Oct)";
            }
        });

        dayWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDaySelection(dayWed);
                selectedDay = "Wednesday (8 Oct)";
            }
        });

        time7am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeSelection(time7am);
                selectedTime = "07:00:00";
            }
        });

        time730am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeSelection(time730am);
                selectedTime = "07:30:00";
            }
        });

        time8am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeSelection(time8am);
                selectedTime = "08:00:00";
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

                intent.putExtra("selected_day", selectedDay);
                intent.putExtra("selected_time", selectedTime);
                intent.putExtra("note", etNote.getText().toString());
                startActivity(intent);
            }
        });

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
    }

    private void updateDaySelection(View selectedView) {
        dayToday.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        dayMon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        dayTues.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        dayWed.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));

        selectedView.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.holo_blue_dark)));
    }

    private void updateTimeSelection(View selectedView) {
        time7am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        time730am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        time8am.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B4E4E4")));
        selectedView.setBackgroundTintList(ColorStateList.valueOf(getColor(android.R.color.holo_blue_dark)));
    }
}