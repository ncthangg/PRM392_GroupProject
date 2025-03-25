package com.example.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.main.interfaces.ApiService;
import com.example.main.models.AuthResponse;
import com.example.main.models.SignInRequest;
import com.example.main.models.SignUpRequest;
import com.example.main.retrofits.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleSignIn";
    private Button btnGoogleSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        //login GG
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI elements
        btnGoogleSignIn = findViewById(R.id.btnGoogle);
//        btnGoogleSignOut = findViewById(R.id.btnGoogleSignOut);

        // Check if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Toast.makeText(getApplicationContext(), "Đăng nhập bằng GG thành công", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();

        }

        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
//        btnGoogleSignOut.setOnClickListener(v -> signOut());
        //End loginGG
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
                                Intent intent = new Intent(SignInActivity.this, ServiceManagerActivity.class);
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
    //login
    // Method to sign in
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // Method to sign out
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(SignInActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "User signed out.");

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to update UI based on login status
//

    @Override
    protected void onResume() {
        super.onResume();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d(TAG, "Google Sign-In thành công: " + account.getEmail());
                    handleGoogleSignIn(account.getEmail(), account.getDisplayName());
                }
            } catch (ApiException e) {
                Log.e(TAG, "Đăng nhập thất bại! Mã lỗi: " + e.getStatusCode());
                Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Xử lý logic đăng ký & đăng nhập khi dùng Google
    private void handleGoogleSignIn(String email, String fullName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        SignUpRequest signUpRequest = new SignUpRequest(
                fullName, "Male", formattedDate, "No Address", email, "string", email
        );

        Call<Void> signUpCall = apiService.signUp(signUpRequest);
        signUpCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Tài khoản mới được tạo, đăng nhập ngay...");
                    signInWithEmail(email, "string");
                } else if (response.code() == 400) {
                    Log.d(TAG, "Tài khoản đã tồn tại, chuyển sang đăng nhập...");
                    signInWithEmail(email, "string");
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể tạo tài khoản!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi mạng khi tạo tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Đăng nhập với email và password
    private void signInWithEmail(String email, String password) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        SignInRequest signInRequest = new SignInRequest(email, password);
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
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng sau khi đăng nhập
                    Intent intent = new Intent(SignInActivity.this, ServiceForCusActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi mạng khi đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //end login
}

