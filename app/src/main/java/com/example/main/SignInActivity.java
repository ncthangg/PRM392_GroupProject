package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.main.interfaces.ApiService;
import com.example.main.models.AuthResponse;
import com.example.main.models.SignInRequest;
import com.example.main.retrofits.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in); // Use your correct layout

        // UI Elements
        TextView signInTextView = findViewById(R.id.buttonSignUp);
        ImageView backButton = findViewById(R.id.btnBack);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        EditText userName = findViewById(R.id.etUserName);
        EditText password = findViewById(R.id.etPassword);

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = userName.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();

                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // API Call
                SignInRequest signInRequest = new SignInRequest(usernameInput, passwordInput);
                Call<AuthResponse> call = apiService.signIn(signInRequest);

                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String accessToken = response.body().getData().getAccessToken();

                            // Lưu token vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ACCESS_TOKEN", accessToken);
                            editor.apply(); // Lưu lại

                            Toast.makeText(getApplicationContext(), "Sign In Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to Main Activity after successful login
                            if(usernameInput.equals("admin")){
                                Intent intent = new Intent(SignInActivity.this, BookingListMechanist.class);
                                startActivity(intent);
                                finish(); // Close current activity
                            }
                            else if(usernameInput.equals("mechanist")){
                                Intent intent = new Intent(SignInActivity.this, BookingListMechanist.class);
                                startActivity(intent);
                                finish(); // Close current activity
                            } else{
                                Intent intent = new Intent(SignInActivity.this, ServiceForCusActivity.class);
                                startActivity(intent);
                                finish(); // Close current activity
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Navigate to Sign-Up Page
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Navigate Back to Main Page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

