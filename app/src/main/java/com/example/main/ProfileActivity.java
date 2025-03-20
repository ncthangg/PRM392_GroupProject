package com.example.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ View bằng findViewById
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnEditAvatar = findViewById(R.id.btnEditAvatar);
        LinearLayout profileOptionsContainer = findViewById(R.id.profileOptionsContainer);

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý sự kiện chỉnh sửa ảnh đại diện
        btnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Chỉnh sửa ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        });

        // Gọi phương thức tạo danh sách Profile Options
        setupProfileOptions(profileOptionsContainer);
    }

    private void setupProfileOptions(LinearLayout container) {
        String[][] options = {
                {"Your Profile", "user_icon"},
                {"Manage Address", "address_icon"},
                {"Payment Methods", "payment_icon"},
                {"My Bookings", "booking_icon"},
                {"My Wallet", "wallet_icon"},
                {"Settings", "settings_icon"},
                {"Help Center", "help_icon"},
                {"Privacy Policy", "privacy_icon"}
        };

        container.removeAllViews();

        for (String[] option : options) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_profile_option, container, false);
            ImageView imgIcon = itemView.findViewById(R.id.imgIcon);
            TextView txtOption = itemView.findViewById(R.id.txtOption);

            int iconResId = getResources().getIdentifier(option[1], "drawable", getPackageName());
            imgIcon.setImageResource(iconResId);
            txtOption.setText(option[0]);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfileActivity.this, "Clicked: " + option[0], Toast.LENGTH_SHORT).show();
                }
            });

            container.addView(itemView);
        }
    }
}
