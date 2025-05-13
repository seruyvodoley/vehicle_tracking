package com.example.vehicletracking.tracking.utils;

import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

public class VehicleLocationSimulator implements LocationProvider {

    private final Handler handler = new Handler();
    private final LatLng initialPosition;
    private final Random random = new Random();
    private LocationCallback callback;
    private boolean isRunning = false;

    public VehicleLocationSimulator(double latitude, double longitude) {
        this.initialPosition = new LatLng(latitude, longitude);
    }

    @Override
    public void start(LocationCallback callback) {
        this.callback = callback;
        isRunning = true;
        simulateMovement();
    }

    @Override
    public void stop() {
        isRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public LatLng getNextLocation() {
        return null;
    }

    private void simulateMovement() {
        handler.postDelayed(() -> {
            if (!isRunning || callback == null) return;

            double latOffset = (random.nextDouble() - 0.5) / 1000;
            double lonOffset = (random.nextDouble() - 0.5) / 1000;

            LatLng newLocation = new LatLng(
                    initialPosition.latitude + latOffset,
                    initialPosition.longitude + lonOffset
            );

            callback.onLocationChanged(newLocation);
            simulateMovement();
        }, 10000); // каждые 2 секунды
    }
}
