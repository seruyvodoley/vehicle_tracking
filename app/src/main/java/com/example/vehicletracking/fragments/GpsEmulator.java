package com.example.vehicletracking.fragments;

import com.google.android.gms.maps.model.LatLng;

public class GpsEmulator {
    private static final LatLng[] route = {
            new LatLng(55.751244, 37.618423), // начальная точка
            new LatLng(55.752121, 37.615619), // следующая
            new LatLng(55.753682, 37.612511)
    };

    private int currentIndex = 0;

    public LatLng getNextLocation() {
        LatLng location = route[currentIndex];
        currentIndex = (currentIndex + 1) % route.length;
        return location;
    }
}
