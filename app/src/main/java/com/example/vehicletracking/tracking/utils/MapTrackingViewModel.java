package com.example.vehicletracking.tracking.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapTrackingViewModel extends ViewModel {

    private Handler handler;
    private Runnable runnable;
    private Marker marker;
    private double currentLat;
    private double currentLng;
    private int routeIndex;
    private Runnable movementTask;


    public void startTracking(double startLat, double startLng, GoogleMap map, String carName) {
        currentLat = startLat;
        currentLng = startLng;

        LatLng startPosition = new LatLng(currentLat, currentLng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 15));
        marker = map.addMarker(new MarkerOptions().position(startPosition).title(carName));

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
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
        handler.post(runnable);
    }

    public void stopTracking() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTracking(); // Очистка при уничтожении ViewModel
    }

    public void RouteSimulateMovement(List<LatLng> routePoints, GoogleMap map, Marker carMarker) {
        this.routeIndex = 0;

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


}
