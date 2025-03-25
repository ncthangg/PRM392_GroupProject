package com.example.main.retrofits;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://103.112.211.244:6995/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Define expected date format
                .create();
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        // Retrieve access token from SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        String accessToken = sharedPreferences.getString("ACCESS_TOKEN", null);

                        // Add Authorization header if the token exists
                        Request.Builder requestBuilder = original.newBuilder();
                        if (accessToken != null) {
                            requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
                        }

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}


