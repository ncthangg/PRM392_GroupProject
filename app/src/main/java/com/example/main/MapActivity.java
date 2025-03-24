package com.example.main;


import static com.example.main.PlatformPositioningProvider.LOG_TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.here.sdk.core.Color;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoCoordinatesUpdate;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.engine.AuthenticationMode;
import com.here.sdk.core.engine.SDKNativeEngine;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.mapview.LineCap;
import com.here.sdk.mapview.LocationIndicator;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapCameraAnimation;
import com.here.sdk.mapview.MapCameraAnimationFactory;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapMeasure;
import com.here.sdk.mapview.MapMeasureDependentRenderSize;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.RenderSize;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Waypoint;
import com.here.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MapActivity extends AppCompatActivity {
    MapView mapView;
    Button btnFlyTo, btnCalculateRoute, btnCurrentLocation;
    MapCamera mapCamera;
    PlatformPositioningProvider platformPositioningProvider;
    RoutingEngine routingEngine;
    MapPolyline currentRoutePolyline;
    GeoCoordinates startGeoCoordinates, storeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeHERESDK();


        setContentView(R.layout.map);


        mapView = findViewById(R.id.map_view);
        btnFlyTo = findViewById(R.id.btn_fly_to);
        btnCurrentLocation = findViewById(R.id.btn_current_location);
        btnCalculateRoute = findViewById(R.id.btn_calculate_route);
        mapView.onCreate(savedInstanceState);
        mapCamera = mapView.getCamera();
        loadMapScene();


        storeLocation = new GeoCoordinates(10.845349, 106.839597);

        // Giảm kích thước ảnh trước khi tạo MapImage

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location);

        if (originalBitmap != null) {
            // Tính toán kích thước mới (1/10 chiều rộng và 1/10 chiều cao sẽ là 1/100 diện tích)
            int newWidth = originalBitmap.getWidth() / 100;  // Giảm 10 lần
            int newHeight = originalBitmap.getHeight() / 100; // Giảm 10 lần

            // Resize ảnh
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

            // Chuyển thành MapImage
            MapImage mapImage = MapImageFactory.fromBitmap(resizedBitmap);

            MapMarker mapMarker = new MapMarker(storeLocation, mapImage);

            mapView.getMapScene().addMapMarker(mapMarker);
        } else {
            Log.e("BitmapError", "Failed to load R.drawable.location");
        }


        //Xem vị trí của cửa hàng
        btnFlyTo.setOnClickListener(v -> {
            flyTo(storeLocation, mapCamera);
        });


        btnCurrentLocation.setOnClickListener(v-> {
            flyTo(startGeoCoordinates, mapCamera);
        });
        btnCalculateRoute.setOnClickListener(v -> calculateRoutes(startGeoCoordinates, storeLocation));
        platformPositioningProvider = new PlatformPositioningProvider(this);
        startLocationUpdates();
    }
    //Khởi tạo HereSDK
    private void initializeHERESDK() {
        String accessKeyID = "mv114_28GkZjLrX9My51CA";
        String accessKeySecret = "YiPethLHGCtM3JlPlJB9L8kTYk_biHVb2FJsBbbF2CnsF2yjzuADkF8m8qb1lk15Y242H6l6_MVbGeT_4x1haw";
        AuthenticationMode authenticationMode = AuthenticationMode.withKeySecret(accessKeyID, accessKeySecret);
        SDKOptions options = new SDKOptions(authenticationMode);


        try {
            Context context = this;
            SDKNativeEngine.makeSharedInstance(context, options);
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of HERE SDK failed: " + e.error.name());
        }
    }


    private void loadMapScene() {
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    double distanceInMeters = 1000;
                    MapMeasure mapMeasureZoom = new MapMeasure(MapMeasure.Kind.DISTANCE_IN_METERS, distanceInMeters);
                    mapView.getCamera().lookAt(
                            new GeoCoordinates(10.845349, 106.839597), mapMeasureZoom);
                } else {
                    Log.d("loadMapScene()", "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }

    private void disposeHERESDK() {
        SDKNativeEngine sdkNativeEngine = SDKNativeEngine.getSharedInstance();
        if (sdkNativeEngine != null) {
            sdkNativeEngine.dispose();
            SDKNativeEngine.setSharedInstance(null);
        }
    }

    private void flyTo(GeoCoordinates geoCoordinates, MapCamera mapCamera) {
        GeoCoordinatesUpdate geoCoordinatesUpdate = new GeoCoordinatesUpdate(geoCoordinates);
        double bowFactor = 1;
        MapCameraAnimation animation =
                MapCameraAnimationFactory.flyTo(geoCoordinatesUpdate, bowFactor, Duration.ofSeconds(1));
        mapCamera.startAnimation(animation);
    }


    //Thay vì dùng maker thì mình thay vị trí hiện tại của mình thành 1 chấm xong như trên google maps
    private void addLocationIndicator(GeoCoordinates geoCoordinates) {
        LocationIndicator locationIndicator = new LocationIndicator();
        locationIndicator.setLocationIndicatorStyle(LocationIndicator.IndicatorStyle.PEDESTRIAN);


        com.here.sdk.core.Location location = new com.here.sdk.core.Location(geoCoordinates);
        location.time = new Date();
        location.bearingInDegrees = 0.0;


        locationIndicator.updateLocation(location);


        // Show the indicator on the map view.
        locationIndicator.enable(mapView);
    }

    //Xác định vị trí hiện tải của người dùng
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }
        platformPositioningProvider.startLocating(new PlatformPositioningProvider.PlatformLocationListener() {
            @Override
            public void onLocationUpdated(Location location) {
                // Log the updated location
                Log.d(LOG_TAG, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                GeoCoordinates geoCoordinates = new GeoCoordinates(location.getLatitude(), location.getLongitude());
                addLocationIndicator(geoCoordinates);
                flyTo(geoCoordinates, mapCamera);
                startGeoCoordinates = geoCoordinates;
            }
        });
    }

    //Tính toán quãng đường từ vị trí hiện tại đến cửa hàng
    public void calculateRoutes(GeoCoordinates startGeoCoordinates, GeoCoordinates destinationGeoCoordinates) {
        Waypoint startWaypoint = new Waypoint(startGeoCoordinates);
        Waypoint destinationWaypoint = new Waypoint(destinationGeoCoordinates);


        List<Waypoint> waypoints =
                new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));


        routingEngine.calculateRoute(
                waypoints,
                new CarOptions(),
                new CalculateRouteCallback() {
                    @Override
                    public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {
                        if (routingError == null) {
                            Route route = routes.get(0);
                            showRouteOnMap(route);
                        } else {
                            Toast.makeText(MapActivity.this, "Lỗi khi tính đường", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Hiển thị đường đi lên bản đồ
    public void showRouteOnMap(Route route) {
        if (currentRoutePolyline != null) {
            mapView.getMapScene().removeMapPolyline(currentRoutePolyline);
        }
        GeoPolyline routeGeoPolyline = route.getGeometry();
        float widthInPixels = 20;
        Color polylineColor = Color.valueOf(0, 0.56f, 0.54f, 0.63f);
        mapCamera.lookAt(startGeoCoordinates);
        mapCamera.zoomTo(10);
        try {
            currentRoutePolyline = new MapPolyline(routeGeoPolyline, new MapPolyline.SolidRepresentation(
                    new MapMeasureDependentRenderSize(RenderSize.Unit.PIXELS, widthInPixels),
                    polylineColor,
                    LineCap.ROUND));
        } catch (MapPolyline.Representation.InstantiationException e) {
            Log.e("MapPolyline Representation Exception:", e.error.name());
        } catch (MapMeasureDependentRenderSize.InstantiationException e) {
            Log.e("MapMeasureDependentRenderSize Exception:", e.error.name());
        }
        mapView.getMapScene().addMapPolyline(currentRoutePolyline);
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }


    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        disposeHERESDK();
        super.onDestroy();
        platformPositioningProvider.stopLocating();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}

