package com.example.vehicletracking.modelview;

import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.fragments.VehicleLocationSimulator;
import com.example.vehicletracking.fragments.VehicleTracker;
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
