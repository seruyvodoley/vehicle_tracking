package com.example.vehicletracking.utils;

import com.google.android.gms.maps.model.LatLng;

public interface LocationCallback {
    void onLocationChanged(LatLng newLocation);
}
