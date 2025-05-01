package com.example.vehicletracking.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarkerManager {
    private GoogleMap googleMap;

    public MapMarkerManager(GoogleMap map) {
        this.googleMap = map;
    }

    public void showCarMarker(LatLng position, String carName) {
        googleMap.clear(); // очищаем старые маркеры
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(carName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }
}
