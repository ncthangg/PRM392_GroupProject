package com.example.main;

import static android.content.Context.LOCATION_SERVICE;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


public class PlatformPositioningProvider implements LocationListener {
    public static final String LOG_TAG = PlatformPositioningProvider.class.getName();
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 100;
    private static final int PERMISSION_REQUEST_CODE = 100;


    private final Context context;
    private LocationManager locationManager;
    @Nullable
    private PlatformLocationListener platformLocationListener;


    public interface PlatformLocationListener {
        void onLocationUpdated(Location location);
    }


    public PlatformPositioningProvider(Context context) {
        this.context = context;
    }


    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        if (platformLocationListener != null) {
            platformLocationListener.onLocationUpdated(location);
        }
    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d(LOG_TAG, "PlatformPositioningProvider enabled.");
    }


    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d(LOG_TAG, "PlatformPositioningProvider disabled.");
    }


    public void startLocating(PlatformLocationListener locationCallback) {
        if (this.platformLocationListener != null) {
            throw new RuntimeException("Please stop locating before starting again.");
        }
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            Log.d(LOG_TAG, "Requested positioning permissions.");
            return; // Dừng lại để chờ người dùng cấp quyền
        }


        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Positioning permissions denied.");
            return;
        }


        this.platformLocationListener = locationCallback;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_INTERVAL_IN_MS, 1, this);
        } else {
            Log.d(LOG_TAG, "Positioning not possible.");
            stopLocating();
        }
    }


    public void stopLocating() {
        if (locationManager == null) {
            return;
        }


        locationManager.removeUpdates(this);
        platformLocationListener = null;
    }
}
