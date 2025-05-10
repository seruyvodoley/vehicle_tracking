package com.example.vehicletracking.tracking.utils;

import com.google.android.gms.maps.model.LatLng;

public interface LocationCallback {
    void onLocationChanged(LatLng newLocation);
}
