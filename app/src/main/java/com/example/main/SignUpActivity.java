package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.main.interfaces.ApiService;
import com.example.main.models.AuthResponse;
import com.example.main.models.SignUpRequest;
import com.example.main.retrofits.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up); // Use your correct layout

        TextView signInTextView = findViewById(R.id.buttonSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        EditText fullName = findViewById(R.id.etFullName);
        EditText email = findViewById(R.id.etEmail);
        EditText userName = findViewById(R.id.etUserName);
        EditText address = findViewById(R.id.etAddress);
        EditText password = findViewById(R.id.etPassword);
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String fullNameText = fullName.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String userNameText = userName.getText().toString().trim();
                String addressText = address.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                // Validate fields
                if (fullNameText.isEmpty() || emailText.isEmpty() || userNameText.isEmpty() || addressText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create sign-up request
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = sdf.format(new Date()); // Convert to "yyyy-MM-dd"

                SignUpRequest signUpRequest = new SignUpRequest(fullNameText, "Male", formattedDate, addressText, userNameText, passwordText, emailText);

                // Call API
                Call<Void> call = apiService.signUp(signUpRequest);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.code() == 201) {  // HTTP 201 Created
                            Toast.makeText(SignUpActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignInActivity
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

}
