package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    private LinearLayout dayToday, dayMon, dayTues, dayWed;
    private TextView time7am, time730am, time8am;
    private EditText etNote;
    private Button btnContinue, btnBack, btnShare, btnFavorite;

    private String selectedDay = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initViews();

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
                selectedTime = "7:00 AM";
            }
        });

        time730am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeSelection(time730am);
                selectedTime = "7:30 AM";
            }
        });

        time8am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeSelection(time8am);
                selectedTime = "8:00 AM";
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
                intent.putExtra("selected_day", selectedDay);
                intent.putExtra("selected_time", selectedTime);
                intent.putExtra("note", etNote.getText().toString().trim());
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
        dayToday.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        dayMon.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        dayTues.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        dayWed.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        selectedView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
    }

    private void updateTimeSelection(View selectedView) {
        time7am.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        time730am.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        time8am.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        selectedView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
    }
}