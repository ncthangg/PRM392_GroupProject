package com.example.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.main.interfaces.ApiService;
import com.example.main.models.ServiceReq;
import com.example.main.retrofits.RetrofitClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import com.example.main.models.ServiceItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etServiceName, etCategory, etWorkerName, etPrice;
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
        etCategory = findViewById(R.id.etCategory);
        etWorkerName = findViewById(R.id.etWorkerName);
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
        String category = etCategory.getText().toString().trim();
        String workerName = etWorkerName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || workerName.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);

        ServiceReq service = new ServiceReq(encodedImage, name, category, price, workerName);

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.createService(service).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddServiceActivity.this, "Dịch vụ đã được thêm!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddServiceActivity.this, "Lỗi khi thêm dịch vụ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddServiceActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
