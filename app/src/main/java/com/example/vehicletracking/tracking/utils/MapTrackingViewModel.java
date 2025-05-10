package com.example.vehicletracking.tracking.utils;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

public class MapTrackingViewModel extends ViewModel {

    private VehicleTracker tracker;

    public void startTracking(double lat, double lon, GoogleMap map, String carName) {
        VehicleLocationSimulator simulator = new VehicleLocationSimulator(lat, lon);
        tracker = new VehicleTracker(simulator, map, carName);
        tracker.startTracking();
    }

    public void stopTracking() {
        if (tracker != null) {
            tracker.stopTracking();
        }
    }
}
