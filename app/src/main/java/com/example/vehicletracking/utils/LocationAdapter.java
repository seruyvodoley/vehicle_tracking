package com.example.vehicletracking.utils;

import android.content.Context;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationAdapter {

    private final FusedLocationProviderClient fusedLocationClient;

    public LocationAdapter(Context context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public FusedLocationProviderClient getClient() {
        return fusedLocationClient;
    }
}
