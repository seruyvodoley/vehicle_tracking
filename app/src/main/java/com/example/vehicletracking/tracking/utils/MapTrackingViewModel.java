package com.example.vehicletracking.tracking.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapTrackingViewModel extends ViewModel {

    private Handler handler;
    private Runnable trackingRunnable;
    private Runnable movementTask;
    private Marker marker;
    private double currentLat;
    private double currentLng;
    private int routeIndex;

    public interface MarkerCallback {
        void onMarkerReady(MarkerOptions markerOptions, String address);
    }

    public void startTracking(double startLat, double startLng, GoogleMap map, String carName) {
        currentLat = startLat;
        currentLng = startLng;

        LatLng startPosition = new LatLng(currentLat, currentLng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 15));
        marker = map.addMarker(new MarkerOptions().position(startPosition).title(carName));

        handler = new Handler(Looper.getMainLooper());
        trackingRunnable = new Runnable() {
            @Override
            public void run() {
                currentLat += 0.0001;
                currentLng += 0.0001;

                LatLng newPosition = new LatLng(currentLat, currentLng);
                if (marker != null) {
                    marker.setPosition(newPosition);
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(trackingRunnable);
    }

    public void stopTracking() {
        if (handler != null && trackingRunnable != null) {
            handler.removeCallbacks(trackingRunnable);
        }
        if (handler != null && movementTask != null) {
            handler.removeCallbacks(movementTask);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTracking();
    }

    public void RouteSimulateMovement(List<LatLng> routePoints, GoogleMap map, Marker carMarker) {
        routeIndex = 0;
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        movementTask = new Runnable() {
            @Override
            public void run() {
                if (routeIndex < routePoints.size()) {
                    LatLng nextPosition = routePoints.get(routeIndex++);
                    carMarker.setPosition(nextPosition);
                    map.animateCamera(CameraUpdateFactory.newLatLng(nextPosition));
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(movementTask);
    }

    public MarkerOptions getCarMarkerOptions(LatLng position) {
        return new MarkerOptions()
                .position(position)
                .title("Авто");
    }

    public void createMarkerWithAddress(LatLng latLng, Context context, MarkerCallback callback) {
        new Thread(() -> {
            final String[] addressHolder = new String[1]; // Обходной способ: массив из одного элемента
            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    addressHolder[0] = addresses.get(0).getAddressLine(0);
                } else {
                    addressHolder[0] = "Адрес не найден";
                }
            } catch (IOException e) {
                addressHolder[0] = "Ошибка геокодера";
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Выбрано место")
                    .snippet(addressHolder[0]);

            new Handler(Looper.getMainLooper()).post(() ->
                    callback.onMarkerReady(markerOptions, addressHolder[0])
            );
        }).start();
    }

}
