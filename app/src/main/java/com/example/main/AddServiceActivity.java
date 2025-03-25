package com.example.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.main.interfaces.ApiService;
import com.example.main.models.Category;
import com.example.main.models.ServiceItem;
import com.example.main.retrofits.RetrofitClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etName, etDescription, etPrice;
    private Spinner spCategory;
    private ImageView ivServiceImage;
    private Button btnAdd, btnPickImage;
    private String encodedImage = "";
    private List<Category> categoryList = new ArrayList<>();
//    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        etName = findViewById(R.id.etServiceName);
        etDescription = findViewById(R.id.etServiceDescription);
        etPrice = findViewById(R.id.etServicePrice);
//        spCategory = findViewById(R.id.spServiceCategory);
        ivServiceImage = findViewById(R.id.ivServiceImage);
        btnAdd = findViewById(R.id.btnAdd);
        btnPickImage = findViewById(R.id.btnPickImage);

//        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(categoryAdapter);

//        fetchCategories();

        btnPickImage.setOnClickListener(v -> openImagePicker());
        btnAdd.setOnClickListener(v -> addService());
    }

//    private void fetchCategories() {
//        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
//        apiService.getCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    categoryList = response.body();
//                    List<String> categoryNames = new ArrayList<>();
//                    for (Category category : categoryList) {
//                        categoryNames.add(category.getName());
//                    }
//                    categoryAdapter.clear();
//                    categoryAdapter.addAll(categoryNames);
//                    categoryAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(AddServiceActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(AddServiceActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });




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
                ivServiceImage.setImageBitmap(bitmap);
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
        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());
        int selectedPosition = spCategory.getSelectedItemPosition();

        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

//        Category selectedCategory = categoryList.get(selectedPosition);
        ServiceItem service = new ServiceItem(encodedImage, name, description, price, "9CA4AE5B-C18D-4115-821F-3A28ED7A416F");

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.createService(service).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddServiceActivity.this, "Service Added!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddServiceActivity.this, "Failed to add service", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddServiceActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
