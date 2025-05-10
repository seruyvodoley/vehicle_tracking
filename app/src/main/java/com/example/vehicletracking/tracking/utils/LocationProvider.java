package com.example.vehicletracking.tracking.utils;

import com.google.android.gms.maps.model.LatLng;

public interface LocationProvider {
    void start(LocationCallback callback);
    void stop();
    LatLng getNextLocation();
}
