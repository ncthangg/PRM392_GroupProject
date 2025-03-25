package com.example.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.main.interfaces.ApiService;
import com.example.main.models.ServiceReq;
import com.example.main.models.Category;
import com.example.main.models.ServiceItem;
import com.example.main.retrofits.RetrofitClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import com.example.main.models.ServiceItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etServiceName, etServiceDes, etPrice;
    private ImageView imgService;
    private Button btnChooseImage, btnSave;
    private String encodedImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        // Ánh xạ view từ XML
        imgService = findViewById(R.id.imgService);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSave);
        etServiceName = findViewById(R.id.etServiceName);
        etServiceDes = findViewById(R.id.etServiceDes);
        etPrice = findViewById(R.id.etPrice);

        // Xử lý chọn ảnh
        btnChooseImage.setOnClickListener(v -> openImagePicker());

        // Xử lý lưu dịch vụ
        btnSave.setOnClickListener(v -> addService());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgService.setImageBitmap(bitmap);
                encodeImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.getEncoder().encodeToString(byteArray);
    }

    private void addService() {
        String name = etServiceName.getText().toString().trim();
        String description = etServiceDes.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra lỗi chuyển đổi số
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // In log kiểm tra dữ liệu
        Log.d("AddService", "Name: " + name);
        Log.d("AddService", "Description: " + description);
        Log.d("AddService", "Price: " + price);

        // Tạo đối tượng ServiceReq
        ServiceReq service = new ServiceReq(
                "https://fixitright.blob.core.windows.net/fixitright/aircondition.jpg",
                name, description, price,
                "9ca4ae5b-c18d-4115-821f-3a28ed7a416f"
        );

        // Gọi API
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.createService(service).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("AddService", "Response Code: " + response.code());

                if (response.isSuccessful()) {
                    Toast.makeText(AddServiceActivity.this, "Dịch vụ đã được thêm!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        Log.e("AddService", "Error Body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("AddService", "Lỗi khi đọc lỗi từ response.errorBody()");
                    }
                    Toast.makeText(AddServiceActivity.this, "Lỗi khi thêm dịch vụ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AddService", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(AddServiceActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
