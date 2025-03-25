
package com.example.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.main.interfaces.ApiService;
import com.example.main.models.ServiceReq;
import com.example.main.models.Category;
import com.example.main.models.ServiceItem;
import com.example.main.retrofits.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.example.main.models.Category;
import com.example.main.models.ServiceItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditServiceActivity extends AppCompatActivity {
    private EditText etName, etPrice;
    private ImageView ivServiceImage;
    private Button btnUpdate, btnDelete;
    private Spinner spCategory;
    private String serviceId;
    private String encodedImage = "";
    private List<Category> categoryList = new ArrayList<>();
//    private ArrayAdapter<String> categoryAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        etName = findViewById(R.id.etServiceName);
        etPrice = findViewById(R.id.etServicePrice);
        ivServiceImage = findViewById(R.id.ivServiceImage);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
//        spCategory = findViewById(R.id.spServiceCategory);

        serviceId = getIntent().getStringExtra("service_id");
        etName.setText(getIntent().getStringExtra("service_name"));
        etPrice.setText(getIntent().getStringExtra("service_price"));

        String imageUrl = getIntent().getStringExtra("service_image");
        Glide.with(this).load(imageUrl).into(ivServiceImage);

        // Khởi tạo adapter trước khi loadCategories()
//        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(categoryAdapter);

        btnUpdate.setOnClickListener(view -> updateService());
        btnDelete.setOnClickListener(view -> deleteService());

        // Gọi API lấy danh mục sau khi Adapter đã khởi tạo
//        loadCategories();
    }

    private void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.getEncoder().encodeToString(byteArray);
    }
    private void updateService() {
        String name = etName.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());

        int selectedPosition = spCategory.getSelectedItemPosition();
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        ServiceReq service = new ServiceReq(encodedImage, name, "", price, "9CA4AE5B-C18D-4115-821F-3A28ED7A416F");

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.updateService(serviceId, service).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditServiceActivity.this, "Service Updated!", Toast.LENGTH_SHORT).show();

                    // Gửi kết quả về MainActivity
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditServiceActivity.this, "Failed to update service", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditServiceActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteService() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        apiService.deleteService(serviceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditServiceActivity.this, "Service Deleted!", Toast.LENGTH_SHORT).show();

                    // Gửi kết quả về MainActivity
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditServiceActivity.this, "Failed to delete service", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditServiceActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void loadCategories() {
//        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
//        apiService.getCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    categoryList.clear();
//                    categoryList.addAll(response.body());
//
//                    List<String> categoryNames = new ArrayList<>();
//                    for (Category category : categoryList) {
//                        categoryNames.add(category.getName());
//                    }
//
//                    // Đảm bảo adapter đã được gán giá trị trước khi cập nhật
//                    if (categoryAdapter != null) {
//                        categoryAdapter.clear();
//                        categoryAdapter.addAll(categoryNames);
//                        categoryAdapter.notifyDataSetChanged();
//                    }
//
//                    // Chọn category phù hợp với service hiện tại
//                    String currentCategoryId = getIntent().getStringExtra("service_category_id");
//                    if (currentCategoryId != null) {
//                        for (int i = 0; i < categoryList.size(); i++) {
//                            if (categoryList.get(i).getId().equals(currentCategoryId)) {
//                                spCategory.setSelection(i);
//                                break;
//                            }
//                        }
//                    }
//                } else {
//                    Toast.makeText(EditServiceActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(EditServiceActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



}
