package com.example.vehicletracking.fragments;

import com.example.vehicletracking.utils.LocationProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VehicleTracker {

    private final LocationProvider provider;
    private final GoogleMap map;
    private final String carName;
    private Marker marker;

    public VehicleTracker(LocationProvider provider, GoogleMap map, String carName) {
        this.provider = provider;
        this.map = map;
        this.carName = carName;
    }

    public void startTracking() {
        provider.start(newLocation -> {
            if (marker == null) {
                marker = map.addMarker(new MarkerOptions()
                        .position(newLocation)
                        .title(carName));
            } else {
                marker.setPosition(newLocation);
            }
        });
    }

    public void stopTracking() {
        provider.stop();
    }
}
